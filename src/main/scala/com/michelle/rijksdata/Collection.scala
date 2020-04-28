package com.michelle.rijksdata

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import io.circe.{Encoder, Decoder, Json, HCursor}
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.implicits._
import org.http4s.{EntityDecoder, EntityEncoder, Method, Uri, Request}
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.Method._
import org.http4s.circe._
import io.circe.optics.JsonPath._

trait Collection[F[_]]{
  def get: F[Collection.Artist]
}

object Collection {
  def apply[F[_]](implicit ev: Collection[F]): Collection[F] = ev

  final case class Artist(elapsedMilliseconds: Int, count: Int) 
  object Artist {
    implicit val artistDecoder: Decoder[Artist] = deriveDecoder[Artist]
    implicit def artistEntityDecoder[F[_]: Sync]: EntityDecoder[F, Artist] = jsonOf
    implicit val artistEncoder: Encoder[Artist] = deriveEncoder[Artist]
    implicit def artistEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Artist] = jsonEncoderOf
  }

  final case class Image(hasImage: Int)
  object Image {
    implicit val imageDecoder: Decoder[Image] = deriveDecoder[Image]
    implicit def imageDecoder[F[_]: Sync]:  EntityDecoder[F, Image] = jsonOf
    implicit val imageEncoder: Encoder[Image] = deriveEncoder[Image]
    implicit def imageEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Image] = jsonEncoderOf
  }

  final case class CollectionError(e: Throwable) extends RuntimeException

  def impl[F[_]: Sync](C: Client[F]): Collection[F] = new Collection[F]{

    val dsl = new Http4sClientDsl[F]{}
    import dsl._
      def get: F[Collection.Artist] = {
        C.expect[Artist](GET(uri"https://www.rijksmuseum.nl/api/nl/collection?key=J5mQRBz3&involvedMaker=Rembrandt+van+Rijn"))
          .adaptError { case t => CollectionError(t) } // Prevent Client Json Decoding Failure Leaking
      }

      val getJson: F[Request[F]] = GET(uri"https://www.rijksmuseum.nl/api/nl/collection?key=J5mQRBz3&involvedMaker=Rembrandt+van+Rijn")

      //val _hasImage = root.countFacets.hasimage.int
      //val getHasImage = _hasImage.getOption(getJson)
  }
}
