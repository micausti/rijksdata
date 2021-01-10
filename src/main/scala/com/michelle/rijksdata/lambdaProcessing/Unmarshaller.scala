package com.michelle.rijksdata.lambdaProcessing

import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage
import com.michelle.rijksdata.lambdaProcessing.Unmarshaller.UnmarshallResult
import io.circe.Decoder
import io.circe.parser.decode


final case class UnmarshallException(message: String, cause: Throwable) extends Exception(message, cause)

trait Unmarshaller[T] {
  def unmarshall(s: SQSMessage): UnmarshallResult[T]
}

object Unmarshaller {
  type UnmarshallResult[T] = Either[UnmarshallException, T]

  def apply[T](f: SQSMessage => UnmarshallResult[T]): Unmarshaller[T] = f.apply

  def unmarshall[T](implicit unmarshaller: Unmarshaller[T]): SQSMessage => UnmarshallResult[T] = unmarshaller.unmarshall
}

object CirceUnmarshaller {

  def unmarshallFromDecodeJson[T](implicit decoder: Decoder[T]): Unmarshaller[T] = new Unmarshaller[T] {
    override def unmarshall(s: SQSMessage): UnmarshallResult[T] =
      decode(s.getBody).left.map(e => UnmarshallException(s"Failed to unmarshall '$s'", e))
  }
}