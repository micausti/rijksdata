package com.michelle.rijksdata.Clients

import cats.effect.IO
import cats.implicits._
import com.michelle.rijksdata.EffectfulLogging
import com.michelle.rijksdata.Models.{MetObjectResult, MetSearchResult}
import org.http4s.Method.GET
import org.http4s.{EntityDecoder, Request, Uri}
import org.http4s.circe.jsonOf
import org.http4s.client.Client

trait MetClient {
  def getSearchResult: IO[MetSearchResult]
  def getObjectResult(metSearchResult: MetSearchResult): IO[List[MetObjectResult]]
}

object MetClient extends EffectfulLogging {

  implicit val metSearchResultEntityDecoder: EntityDecoder[IO, MetSearchResult] = jsonOf

  def apply(client: Client[IO], baseUri: Uri): MetClient = {
    new MetClient {
      override def getSearchResult: IO[MetSearchResult] = {
        val url = baseUri / "public" / "collection" / "v1" / "search"
        val urlWithQuery = url.withQueryParam("q", "woodcut")
        client.run(Request[IO](GET, urlWithQuery)).use{
          case response if response.status.isSuccess =>
          response.as[MetSearchResult]
          case _ => MetSearchResult(List.empty).pure[IO]
        }
      }

      implicit val metObjectResultEntityDecoder: EntityDecoder[IO, MetObjectResult] = jsonOf

      override def getObjectResult(metSearchResult: MetSearchResult): IO[List[MetObjectResult]] = {
        val url = baseUri/ "public" / "collection" / "v1" / "objects"
        val urls = metSearchResult.objectIDs.map(id => Uri(path = (url / id.toString).path))
        urls.map(u =>
        client.run(Request[IO](GET, u)).use {
          case response if response.status.isSuccess =>
          response.as[MetObjectResult]
          case _ => MetObjectResult("", "", "", "").pure[IO]
        })
      }.sequence
    }
  }
}
