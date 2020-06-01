package com.michelle.rijksdata

import cats.implicits._
import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import org.http4s.implicits._
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.Method._
import org.http4s.circe._



trait Rijksdata {
  def get(i: Rijksdata.ItemId) : IO[Rijksdata.Item]
}

object Rijksdata {
  def apply(implicit ev: Rijksdata): Rijksdata = ev

  final case class Item(details: String) extends AnyVal
  object Item {
    implicit val rijksDecoder: Decoder[Item] = deriveDecoder[Item]
    implicit def rijksEntityDecoder[Sync]: EntityDecoder[IO, Item] =
      jsonOf
    implicit val rijksEncoder: Encoder[Item] = deriveEncoder[Item]
    implicit def rijksEntityEncoder[Applicative]: EntityEncoder[IO, Item] =
      jsonEncoderOf
  }

  final case class RijksError(e: Throwable) extends RuntimeException
  final case class ItemId(value:String)

  def impl[Sync](C: Client[IO]): Rijksdata = new Rijksdata{
    val dsl = new Http4sClientDsl[IO]{}
    import dsl._
    def get(i: Rijksdata.ItemId): IO[Rijksdata.Item] = {
      C.expect[Item](GET(uri"https://www.rijksmuseum.nl/api/nl/collection/SK-A-4/?key=J5mQRBz3"))
        .adaptError{ case t => RijksError(t)} // Prevent Client Json Decoding Failure Leaking
    }
  }
}