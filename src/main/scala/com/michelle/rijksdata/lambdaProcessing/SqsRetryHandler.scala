package com.michelle.rijksdata.lambdaProcessing

import java.net.URL
import java.util.UUID
import cats.effect.IO
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage
import com.amazonaws.services.sqs.AmazonSQS
import com.michelle.rijksdata.EffectfulLogging
import com.michelle.rijksdata.lambdaProcessing.Unmarshaller
import org.slf4j.MDC

import scala.concurrent.duration.FiniteDuration

final case class RetryException(message: String) extends Exception(message)

final case class DeadLetterException(message: String) extends Exception(message)

final case class SqsRetryConfig(
                                 queueUrl: URL,
                                 deadLetterQueueUrl: URL,
                                 sqsClient: AmazonSQS,
                                 timeoutBetweenRequeue: FiniteDuration
                               )

abstract class SqsRetryHandler[T](implicit unmarshaller: Unmarshaller[T]) {
  val config: SqsRetryConfig
  val process: T => IO[ConsumeAction]
  def handler(sqsEvent: SQSEvent): Unit =
    SqsRetryHandler.retryHandler(config, process)(unmarshaller)(sqsEvent)
}

object SqsRetryHandler extends EffectfulLogging {

  def retryHandler[T](
                       config: SqsRetryConfig,
                       process: T => IO[ConsumeAction],
                     )(implicit unmarshaller: Unmarshaller[T]): SQSEvent => Unit = { event =>
    val result = for {
      message       <- incomingMessage(event)
      _             = MDC.clear()
      correlationId <- IO(correlationIdGenerator(message))
      _             = MDC.put(SQSUtils.CorrelationIdKey, correlationId)
      _             <- IO(logger.info("Received message: " + message))
      action <- unmarshall(message) match {
        case Left(deadLetter) => IO.pure(deadLetter)
        case Right(t)         => (process andThen handleErrorWithRequeue)(t)
      }
      _ <- handleConsumeAction(
        message,
        config.sqsClient,
        config.timeoutBetweenRequeue,
        config.queueUrl,
        config.deadLetterQueueUrl
      )(action)
    } yield ()

    result.unsafeRunSync()
  }

  def correlationIdGenerator(msg: SQSMessage): String = {
    val annotation: Option[SQSEvent.MessageAttribute] = msg.getMessageAttributes.asScala.get(SQSUtils.CorrelationIdKey)
    annotation
      .map(_.getStringValue)
      .fold(UUID.randomUUID().toString)(identity)
  }

  private val incomingMessage: SQSEvent => IO[SQSMessage] = { event =>
    event.getRecords.asScala.toList match {
      case Nil            => IO.raiseError(new Exception("Zero messages to process"))
      case message :: Nil => IO(message)
      case messages       => IO.raiseError(new Exception(s"Expected 1 message, but got ${messages.size}"))
    }
  }

  private val handleErrorWithRequeue: IO[ConsumeAction] => IO[ConsumeAction] = action =>
    action.handleErrorWith(error => IO.pure(Requeue(error.getMessage, Some(error))))

  private def unmarshall[T](message: SQSMessage)(implicit unmarshaller: Unmarshaller[T]): Either[DeadLetter, T] =
    unmarshaller.unmarshall(message) match {
      case Left(error) => Left(DeadLetter(error.message, Some(error)))
      case Right(t) => Right(t)
    }


  private def handleConsumeAction(
                                   sqsMessage: SQSMessage,
                                   sqsClient: AmazonSQS,
                                   visibilityTimeout: FiniteDuration,
                                   queueUrl: URL,
                                   deadLetterQueueUrl: URL
                                 ): ConsumeAction => IO[Unit] = { consumeAction =>
    def deadLetter: IO[Unit] =
      IO(sqsClient.sendMessage(deadLetterQueueUrl.toExternalForm, sqsMessage.getBody))
        .map(_ => logger.info(s"DeadLetter message sent to ${deadLetterQueueUrl.toExternalForm}"))

    def requeue: IO[Unit] =
      IO(sqsClient.changeMessageVisibility(queueUrl.toExternalForm, sqsMessage.getReceiptHandle, visibilityTimeout.toSeconds.toInt)).attempt
        .map {
          case Right(_) =>
            logger.info(s"Requeued message ${sqsMessage.getMessageId} onto $queueUrl with visibility timeout of ${visibilityTimeout.toSeconds}")
          case Left(error) =>
            logger.error(
              s"Requeue: Unable to set message visibility timeout for ${sqsMessage.getMessageId} to ${visibilityTimeout.toSeconds.toInt} seconds for queue ${queueUrl.toExternalForm}: ${error.getMessage}",
              error
            )
        }

    consumeAction match {
      case DeadLetter(message, Some(error)) =>
        logger.error(s"DeadLetter: Reason: $message", error)
        deadLetter
      case DeadLetter(message, None) =>
        logger.error(s"DeadLetter: Reason: $message")
        deadLetter
      case Requeue(message, Some(error)) =>
        logger.error(s"Requeue: Reason: $message", error)
        requeue.flatMap(_ => IO.raiseError(error))
      case Requeue(message, None) =>
        logger.error(s"Requeue: Reason: $message")
        requeue.flatMap(_ => IO.raiseError(RetryException(message)))
      case Ack => IO.unit
    }
  }
}
