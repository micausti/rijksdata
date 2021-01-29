package com.michelle.rijksdata

import cats.effect.{ContextShift, IO}
import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
import com.michelle.rijksdata.lambdaProcessing.{ConsumeAction, SqsRetryConfig, SqsRetryHandler, Unmarshaller}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

abstract class Lambda[T:Unmarshaller]
extends SqsRetryHandler[T]
with EffectfulLogging {

  implicit val ec: ExecutionContextExecutor = ExecutionContext.global
  implicit val cs: ContextShift[IO] = IO.contextShift(ec)
  lazy val sqsClient: AmazonSQS = AmazonSQSClientBuilder.standard().build

//incoming queues
  override val config: SqsRetryConfig = SqsRetryConfig(
    queueUrl = ???,
    deadLetterQueueUrl = ???,
    sqsClient,
    timeoutBetweenRequeue = 10.seconds
  )



  override val process: T => IO[ConsumeAction] = {
???
  }

}
