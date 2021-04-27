package com.michelle.rijksdata.Clients

import cats.effect.{ContextShift, IO}
import cats.implicits._
import com.michelle.rijksdata.EffectfulLogging
import com.michelle.rijksdata.Models.{MetObjectResult, MetSearchResult}
import org.http4s.{EntityDecoder, Request, Uri}
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import org.http4s.dsl.io._

trait MetClient {
  def getSearchResult: IO[MetSearchResult]
  def getObjectResult(metSearchResult: MetSearchResult): IO[List[MetObjectResult]]
}

object MetClient extends EffectfulLogging {

  implicit val metSearchResultEntityDecoder: EntityDecoder[IO, MetSearchResult] = jsonOf

  def apply(client: Client[IO], baseUri: Uri)(implicit cs: ContextShift[IO]): MetClient =
    new MetClient {
      override def getSearchResult: IO[MetSearchResult] = {
        val url          = baseUri / "public" / "collection" / "v1" / "search"
        val urlWithQuery = url.withQueryParam("q", "sunflowers")
        logger.info(s"url with query $urlWithQuery") >> client
          .run(Request[IO](GET, urlWithQuery))
          .use {
            case response if response.status.isSuccess =>
              response.as[MetSearchResult]
            case _ => MetSearchResult(List.empty).pure[IO]
          }
          .flatTap(response => logger.info(s"response $response"))
      }

      implicit val metObjectResultEntityDecoder: EntityDecoder[IO, MetObjectResult] = jsonOf

      override def getObjectResult(metSearchResult: MetSearchResult): IO[List[MetObjectResult]] = {
        val objects = metSearchResult.objectIDs.map(o => o.toString)
        def getObject(objectId: String): IO[MetObjectResult] = {
          val target = Uri.uri("https://collectionapi.metmuseum.org/public/collection/v1/objects") / objectId
          client.expect[MetObjectResult](target)
        }
        objects.parTraverse(getObject)
      }
    }
}
