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



trait Rijksdatas {
  def get : IO[Rijksdatas.Rijksdata]
}

object Rijksdatas {
  def apply(implicit ev: Rijksdatas): Rijksdatas = ev

  final case class Rijksdata(joke: String) extends AnyVal
  object Rijksdata {
    implicit val rijksDecoder: Decoder[Rijksdata] = deriveDecoder[Rijksdata]
    implicit def rijksEntityDecoder[Sync]: EntityDecoder[IO, Rijksdata] =
      jsonOf
    implicit val rijksEncoder: Encoder[Rijksdata] = deriveEncoder[Rijksdata]
    implicit def rijksEntityEncoder[Applicative]: EntityEncoder[IO, Rijksdata] =
      jsonEncoderOf
  }

  final case class RijksError(e: Throwable) extends RuntimeException
  final case class ItemId(value:String)

//  def impl[Sync](C: Client[IO]): Rijksdata = new Rijksdata{
//    val dsl = new Http4sClientDsl[IO]{}
//    import dsl._
//    def get(i: Rijksdata.ItemId): IO[Rijksdata.Item] = {
//      C.expect[Item](GET(uri"https://www.rijksmuseum.nl/api/nl/collection/SK-A-4/?key=J5mQRBz3"))
//        .adaptError{ case t => RijksError(t)} // Prevent Client Json Decoding Failure Leaking

  def impl[Sync](C: Client[IO]): Rijksdatas = new Rijksdatas{
    val dsl = new Http4sClientDsl[IO]{}
    import dsl._
    def get: IO[Rijksdatas.Rijksdata] = {
      C.expect[Rijksdata](GET(uri"https://icanhazdadjoke.com/"))
        .adaptError{ case t => RijksError(t)} // Prevent Client Json Decoding Failure Leaking

      //https://www.rijksmuseum.nl/api/nl/collection/SK-A-4/?key=J5mQRBz3
    }
  }
}