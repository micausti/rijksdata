package com.michelle.rijksdata

import cats.effect.IO
import cats.implicits._
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import io.circe.optics.JsonPath._
import org.http4s.Method._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits._
import org.http4s.{EntityDecoder, EntityEncoder}



trait Details {
  def get: IO[Details.Detail]
}

object Details {
  def apply(implicit ev: Details): Details = ev

  final case class Detail(elapsedMilliseconds: Int)
  object Detail {

    implicit val detailDecoder: Decoder[Detail] = deriveDecoder[Detail]
    implicit def detailEntityDecoder[Sync]: EntityDecoder[IO, Detail] =
      jsonOf
    implicit val detailEncoder: Encoder[Detail] = deriveEncoder[Detail]
    implicit def detailEntityEncoder[Applicative]: EntityEncoder[IO, Detail] =
      jsonEncoderOf
  }

  final case class DetailError(e: Throwable) extends RuntimeException

  def impl[Sync](C: Client[IO]): Details = new Details{
    val dsl = new Http4sClientDsl[IO]{}
    import dsl._
    def get: IO[Details.Detail] = {
      C.expect[Detail](GET(uri"https://www.rijksmuseum.nl/api/nl/collection/SK-A-4/?key=J5mQRBz3"))
        .adaptError{ case t => DetailError(t)} // Prevent Client Json Decoding Failure Leaking
    }
  }
}