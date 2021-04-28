package com.michelle.rijksdata.Clients

import cats.effect.{ContextShift, IO}
import com.michelle.rijksdata.EffectfulLogging
import com.michelle.rijksdata.Models.{AICItemResult, AICSearchResponse}
import org.http4s.{EntityDecoder, Uri}
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import cats.implicits._

trait AICClient {
  def getSearchResult: IO[AICSearchResponse]
  def getItemResult(aicSearchResponse: AICSearchResponse): IO[List[AICItemResult]]
  def prepareImageUrls(itemResult: List[AICItemResult]): List[String]
  def getImageUrls(itemResults: List[AICItemResult]): IO[List[String]]
}

object AICClient extends EffectfulLogging {

  implicit val AICSearchResponseEntityDecoder: EntityDecoder[IO, AICSearchResponse] = jsonOf

  def apply(client: Client[IO], baseUri: Uri)(implicit cs: ContextShift[IO]): AICClient =
    new AICClient {
      override def getSearchResult: IO[AICSearchResponse] = {
        val url          = baseUri / "artworks" / "search"
        val urlWithQuery = url.withQueryParam("q", "woodcut")
        client.expect[AICSearchResponse](urlWithQuery)
      }

      implicit val AICItemResultEntityDecoder: EntityDecoder[IO, AICItemResult] = jsonOf

      override def getItemResult(aicSearchResponse: AICSearchResponse): IO[List[AICItemResult]] = {
        val objects = aicSearchResponse.data.map(_.api_link.split("/").last)
        def getObject(objectId: String): IO[AICItemResult] = {
          val target = baseUri/ "artworks" / objectId
          client.expect[AICItemResult](target)
        }
        objects.parTraverse(getObject)
      }

      override def prepareImageUrls(itemResults: List[AICItemResult]): List[String] = {
        val baseUrl = itemResults.map(_.config.iiif_url).head
        val images  = itemResults.map(_.data.image_id)
        images.map(imageId => baseUrl.concat(imageId).concat("/full/843,/0/default.jpg"))
      }

      override def getImageUrls(itemResults: List[AICItemResult]): IO[List[String]] = {
        val baseUrl = itemResults.map(_.config.iiif_url).head
        val images  = itemResults.map(_.data.image_id)
        def getUrl(imageId: String): IO[String] = {
            Uri.fromString(baseUrl) match {
            case Right(value) => client.expect[String](value / imageId / "full" / "843," / "0"/ "default.jpg")
            case Left(f) => IO("")
          }
        }
        images.parTraverse(getUrl)
      }
    }
}
