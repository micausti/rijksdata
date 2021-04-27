package com.michelle.rijksdata.Clients

import cats.effect.{ContextShift, IO}
import cats.implicits.catsSyntaxApplicativeId
import com.michelle.rijksdata.EffectfulLogging
import com.michelle.rijksdata.Models.{AICItemResult, AICSearchResponse, ItemConfig, ItemData}
import org.http4s.Method.GET
import org.http4s.{EntityDecoder, Request, Uri}
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import cats.implicits._

trait AICClient {
  def getSearchResult: IO[AICSearchResponse]
  def getItemResult(aicSearchResponse: AICSearchResponse): IO[List[AICItemResult]]
  def getImageUrls(itemResult: List[AICItemResult]): IO[List[String]]
}

object AICClient extends EffectfulLogging {

  implicit val AICSearchResponseEntityDecoder: EntityDecoder[IO, AICSearchResponse] = jsonOf

  def apply(client: Client[IO], baseUri: Uri)(implicit cs: ContextShift[IO]): AICClient =
    new AICClient {
      override def getSearchResult: IO[AICSearchResponse] = {
        val url          = baseUri / "artworks" / "search"
        val urlWithQuery = url.withQueryParam("q", "woodcut")
        val response     = client.expect[AICSearchResponse](urlWithQuery)
        response.flatTap(response => logger.info(s"response $response"))
      }

      implicit val AICItemResultEntityDecoder: EntityDecoder[IO, AICItemResult] = jsonOf

      override def getItemResult(aicSearchResponse: AICSearchResponse): IO[List[AICItemResult]] = {
        val objects = aicSearchResponse.data.map(_.api_link.split("/").last)
        def getObject(objectId: String): IO[AICItemResult] = {
          val target = Uri.uri("https://api.artic.edu/api/v1/artworks") / objectId
          logger.info(s"target $target") >> client.expect[AICItemResult](target)
        }
        objects.parTraverse(getObject).flatTap(response => logger.info(s"response $response"))
      }

      override def getImageUrls(itemResults: List[AICItemResult]): IO[List[String]] = {
        val baseUrl = itemResults.map(_.config.iiif_url)
        val images  = itemResults.map(_.data.image_id)
        def getUrl(imageId: String): IO[String] = {
          val target = Uri.uri("https://www.artic.edu/iiif/2") / imageId / "full" / "843," / "0"/ "default.jpg"
          val response = logger.info(s"target $target") >> client.expect[String](target)
          response.flatTap(response => logger.info(s"response $response"))
        }
        images.parTraverse(getUrl)
      }
    }
}
