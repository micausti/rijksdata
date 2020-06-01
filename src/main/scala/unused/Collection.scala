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

trait Collection{
  def get: IO[Collection.Artist]
}

object Collection {
  def apply(implicit ev: Collection): Collection = ev

  final case class Artist(elapsedMilliseconds: Int, count: Int)

  object Artist {
    implicit val artistDecoder: Decoder[Artist] = deriveDecoder[Artist]

    implicit def artistEntityDecoder[Sync]: EntityDecoder[IO, Artist] = jsonOf

    implicit val artistEncoder: Encoder[Artist] = deriveEncoder[Artist]

    implicit def artistEntityEncoder[Applicative]: EntityEncoder[IO, Artist] = jsonEncoderOf
  }

  final case class CollectionError(e: Throwable) extends RuntimeException

  def impl[Sync](C: Client[IO]): Collection = new Collection {

    val dsl = new Http4sClientDsl[IO] {
    }

    import dsl._

    def get: IO[Collection.Artist] = {
      C.expect[Artist](GET(uri"https://www.rijksmuseum.nl/api/nl/collection?key=J5mQRBz3&involvedMaker=Rembrandt+van+Rijn"))
        .adaptError {
          case t => CollectionError(t)
        } // Prevent Client Json Decoding Failure Leaking
    }
  }
}
//    val request: F[Request[F]] = (GET(uri"https://www.rijksmuseum.nl/api/nl/collection?key=J5mQRBz3&involvedMaker=Rembrandt+van+Rijn"))
//    C.fetch[Image](request) {r =>
//      r.status match {
//        case Status.Ok =>
//          for {
//          json <- r.as[Json]
//          image <- root.countFacets.hasimage.int
//              .getOption(json)
//              .map(i => Image(i)).
//          } yield image

