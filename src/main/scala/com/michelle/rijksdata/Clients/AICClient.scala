package com.michelle.rijksdata.Clients

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import com.michelle.rijksdata.EffectfulLogging
import com.michelle.rijksdata.Models.{
  AICItemResult,
  AICSearchResponse,
  ItemConfig,
  ItemData
}
import org.http4s.Method.GET
import org.http4s.{EntityDecoder, Request, Uri}
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import cats.implicits._

trait AICClient {
  def getSearchResult: IO[AICSearchResponse]
  def getItemResult(
      aicSearchResponse: AICSearchResponse): IO[List[AICItemResult]]
}

object AICClient extends EffectfulLogging {

  implicit val AICSearchResponseEntityDecoder
    : EntityDecoder[IO, AICSearchResponse] = jsonOf

  def apply(client: Client[IO], baseUri: Uri): AICClient = {
    new AICClient {
      override def getSearchResult: IO[AICSearchResponse] = {
        val url = baseUri / "search" / "artworks"
        val urlWithQuery = url.withQueryParam("q", "woodcut")
        client.run(Request[IO](GET, urlWithQuery)).use {
          case response if response.status.isSuccess =>
            response.as[AICSearchResponse]
          case _ => AICSearchResponse(List.empty).pure[IO]
        }
      }

      implicit val AICItemResultEntityDecoder
        : EntityDecoder[IO, AICItemResult] = jsonOf

      override def getItemResult(
          aicSearchResponse: AICSearchResponse): IO[List[AICItemResult]] = {
        val urls = aicSearchResponse.data.map(l => Uri(path = l.api_link))
        urls.map(u =>
          client.run(Request[IO](GET, u)).use {
            case response if response.status.isSuccess =>
              response.as[AICItemResult]
            case _ =>
              AICItemResult(ItemData("", "", "", ""), ItemConfig("")).pure[IO]
        })
      }.sequence
    }
  }
}
