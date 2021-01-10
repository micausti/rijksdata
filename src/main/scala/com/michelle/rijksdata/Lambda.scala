package com.michelle.rijksdata

import cats.effect.{ContextShift, IO}
import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
import com.michelle.rijksdata.lambdaProcessing.{ConsumeAction, SqsRetryConfig, SqsRetryHandler, Unmarshaller}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

abstract class Lambda[T:Unmarshaller]
extends SqsRetryHandler[T]
with EffectfulLogging {

  implicit val ec: ExecutionContextExecutor = ExecutionContext.global
  implicit val cs: ContextShift[IO] = IO.contextShift(ec)
  lazy val sqsClient: AmazonSQS = AmazonSQSClientBuilder.standard().build


  //todo decide if this should go to sqs queue or have lambda handle the failures directly
  override val config: SqsRetryConfig = SqsRetryConfig(
    queueUrl = ???,
    deadLetterQueueUrl = ???,
    sqsClient,
    timeoutBetweenRequeue = ???
  )

  override val process: T => IO[ConsumeAction] = {
???
  }

}
