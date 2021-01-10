package com.michelle.rijksdata.lambdaProcessing

import io.circe.Encoder
import io.circe.syntax._

trait Marshaller[T] {
  def marshall(t: T): String
}

object Marshaller {

  def apply[T](f: T => String): Marshaller[T] = apply(f)

  def marshall[T](implicit marshaller: Marshaller[T]): T => String = marshaller.marshall
}

object CirceMarshaller {

  def marshallFromEncodeJson[T](implicit encoder: Encoder[T]) = new Marshaller[T] {
    override def marshall(t: T): String = t.asJson.noSpaces
  }
}
