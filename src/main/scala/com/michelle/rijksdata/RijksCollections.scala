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

trait RijksCollections[F[_]] {
  def get: F[RijksCollections.RijksCollection]
}

object RijksCollections {
  def apply[F[_]](implicit ev: RijksCollections[F]): RijksCollections [F] = ev

  final case class RijksCollection(collection: String) extends AnyVal
  object RijksCollection {
    implicit val collectionDecoder: Decoder[RijksCollection] = deriveDecoder[RijksCollection]
    implicit def collectionEntityDecoder[F[_]: Sync]: EntityDecoder[F, RijksCollection] = jsonOf
    implicit val collectionEncoder: Encoder[RijksCollection] = deriveEncoder[RijksCollection]
    implicit def collectionEntityEncoder[F[_]: Applicative]: EntityEncoder[F, RijksCollection] = jsonEncoderOf
  }

  final case class RijksCollectionError(e: Throwable) extends RuntimeException

  def impl[F[_]: Sync](C: Client[F]): RijksCollections[F] = new RijksCollections[F]{
    val dsl = new Http4sClientDsl[F] {}
    import dsl._
    def get: F[RijksCollections.RijksCollection] = {
      C.expect[RijksCollection](GET(uri"https://www.rijksmuseum.nl/api/nl/collection?key=J5mQRBz3&involvedMaker=Rembrandt+van+Rijn"))
        .adaptError {case t => RijksCollectionError(t)} //prevent client json decoding failure
    }
  }
}