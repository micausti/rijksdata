package unused

import cats.effect.IO
import io.circe.Json
import io.circe.optics.JsonPath._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.{Request, Uri, _}


sealed trait Image {
  def value: String
}

case object HD extends Image {
  override def value: String = "HD"
}

case object SD extends Image {
  override def value: String = "SD"
}

trait ImageClient {
 def formatFor: IO[Option[Image]]
}

object ImageClient  {

  val url = Uri.unsafeFromString("https://www.rijksmuseum.nl/api/nl/collection?key=J5mQRBz3&involvedMaker=Rembrandt+van+Rijn")

  def apply(client: Client[IO]) = new ImageClient {

    override def formatFor: IO[Option[Image]] = {
      val request: Request[IO] = Request[IO](uri = url)
      client.fetch[Option[Image]](request) { i =>
        i.status match {
          case Status.Ok =>
            for {
              json <- i.as[Json]
              image <- root.countFacets.hasimage.int
                  .getOption(json)
                  .fold[IO[Int]](IO.raiseError(new RuntimeException("some error")))(IO.pure)
                  .map(h => Some(formatImage(h)))
            } yield image
          case _ => IO.raiseError(new RuntimeException("some error"))
        }
      }
    }

    def formatImage(img: Int) = if (img > 1000) HD else SD
  }
}
