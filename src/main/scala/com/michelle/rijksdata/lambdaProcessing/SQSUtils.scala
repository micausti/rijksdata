package com.michelle.rijksdata.lambdaProcessing

import java.net.URL
import cats.effect.IO
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.{MessageAttributeValue, SendMessageRequest}
import com.michelle.rijksdata.EffectfulLogging
import org.slf4j.MDC

object SQSUtils extends EffectfulLogging {

  val CorrelationIdKey = "correlationId"

  def correlationId: Option[String] = Option(MDC.get(CorrelationIdKey))

  type SQSPublisher[T] = T => IO[Unit]

  type SQSPublisherWithHeaders[T] = (T, Map[String, String]) => IO[Unit]

  def publisher[T](sqsClient: AmazonSQS)(queueUrl: URL)(message: T)(implicit marshaller: Marshaller[T]): IO[Unit] = {
    publisherWithHeaders(sqsClient)(queueUrl)(message, Map.empty)
  }

  def publisherWithHeaders[T](sqsClient: AmazonSQS)(queueUrl: URL)(message: T, headers:Map[String, String])(implicit marshaller: Marshaller[T]): IO[Unit] = {
    val marshalledMessage = marshaller.marshall(message)

    val sendMessageRequest = new SendMessageRequest(queueUrl.toExternalForm, marshalledMessage)
    headers.map{ case(key, value) => sendMessageRequest.addMessageAttributesEntry(key, new MessageAttributeValue().withStringValue(value).withDataType("String")) }

    Option(MDC.get(CorrelationIdKey)).foreach { correlationId =>
      sendMessageRequest
        .addMessageAttributesEntry(CorrelationIdKey, new MessageAttributeValue().withStringValue(correlationId).withDataType("String"))
    }

    IO(sqsClient.sendMessage(sendMessageRequest)).flatMap { result =>
      if (result.getMessageId.nonEmpty) IO(logger.info(s"Sent SQS Message '$marshalledMessage' to ${queueUrl.toExternalForm}"))
      else IO.raiseError(new Exception(s"Failed to retrieve an SQS messageId when publishing message $message to ${queueUrl.toExternalForm}"))
    }
  }
}

