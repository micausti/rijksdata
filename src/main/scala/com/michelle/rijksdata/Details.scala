package com.michelle.rijksdata

import cats.effect.IO
import com.michelle.rijksdata.Details.ObjectNumber
import io.circe.generic.semiauto._
import io.circe.optics.JsonPath.root
import io.circe.{Decoder, Encoder, Json}
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits._
import org.http4s.{EntityDecoder, EntityEncoder, Request, _}

trait Details {
  def get(objectNumber: ObjectNumber): IO[Details.Detail]
}

object Details {
  def apply(implicit ev: Details): Details = ev

  final case class ObjectNumber(value:String)
  final case class Detail(title:String, artist: String, url:String, description:String)

  object Detail {

    implicit val detailDecoder: Decoder[Detail] = deriveDecoder[Detail]
    implicit def detailEntityDecoder[Sync]: EntityDecoder[IO, Detail] =
      jsonOf
    implicit val detailEncoder: Encoder[Detail] = deriveEncoder[Detail]
    implicit def detailEntityEncoder[Applicative]: EntityEncoder[IO, Detail] =
      jsonEncoderOf
  }


  def createUrl(objectNumber:ObjectNumber) = {
    val baseUri = uri"https://www.rijksmuseum.nl/api/en"
    val collectionUrl = baseUri / "collection" / objectNumber.value
    collectionUrl.withQueryParam("key", "J5mQRBz3") //TODO get a new key and keep somehwere in secrets config
  }

  def impl[Sync](C: Client[IO]): Details = new Details {
    val dsl = new Http4sClientDsl[IO] {}

    //TODO look at changing this to use optionT
    def get(objectNumber: ObjectNumber): IO[Details.Detail] = {
      val request: Request[IO] = Request[IO](uri = createUrl(objectNumber))
      C.fetch[Details.Detail](request) { d =>
        d.status match {
          case Status.Ok =>
            for {
              json <- d.as[Json]
              title <- root.artObject.title.string
                .getOption(json)
                .fold[IO[String]](IO.raiseError(new RuntimeException("can't find title")))(
                  IO.pure)
              artist <- root.artObject.principalMaker.string
                  .getOption(json)
                  .fold[IO[String]](IO.raiseError(new RuntimeException("can't find artist")))(
                    IO.pure)
              url <- root.artObject.webImage.url.string
                .getOption(json)
                .fold[IO[String]](IO.raiseError(new RuntimeException("can't find url")))(
                  IO.pure)
              description <- root.artObject.plaqueDescriptionEnglish.string
                .getOption(json)
                .fold[IO[String]](IO.raiseError(new RuntimeException("can't find description")))(
                  IO.pure)
              detail = Detail(title, artist, url, description)
            } yield detail
          case _ => IO.raiseError(new RuntimeException("bad request"))
        }
      }
    }
  }
}
