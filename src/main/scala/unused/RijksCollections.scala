package unused

import cats.effect.IO
import cats.implicits._
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.http4s.Method._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits._
import org.http4s.{EntityDecoder, EntityEncoder}

trait RijksCollections {
  def get: IO[RijksCollections.RijksCollection]
}

object RijksCollections {
  def apply(implicit ev: RijksCollections): RijksCollections = ev

  final case class RijksCollection(collection: String) extends AnyVal
  object RijksCollection {
    implicit val collectionDecoder: Decoder[RijksCollection] = deriveDecoder[RijksCollection]
    implicit def collectionEntityDecoder[Sync]: EntityDecoder[IO, RijksCollection] = jsonOf
    implicit val collectionEncoder: Encoder[RijksCollection] = deriveEncoder[RijksCollection]
    implicit def collectionEntityEncoder[Applicative]: EntityEncoder[IO, RijksCollection] = jsonEncoderOf
  }

  final case class RijksCollectionError(e: Throwable) extends RuntimeException

  def impl[Sync](C: Client[IO]): RijksCollections = new RijksCollections {
    val dsl = new Http4sClientDsl[IO] {}
    import dsl._
    def get: IO[RijksCollections.RijksCollection] = {
      C.expect[RijksCollection](GET(uri"https://www.rijksmuseum.nl/api/nl/collection?key=J5mQRBz3&involvedMaker=Rembrandt+van+Rijn"))
        .adaptError {case t => RijksCollectionError(t)} //prevent client json decoding failure
    }
  }
}