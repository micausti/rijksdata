package com.michelle.rijksdata.Clients

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import com.michelle.rijksdata.EffectfulLogging
import com.michelle.rijksdata.Models.CollectionResponse
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.{EntityDecoder, Request, _}
import org.http4s.dsl.io.GET

trait RijksdataClient {
  def getObjectWithTechnique: IO[CollectionResponse]
}

object RijksdataClient extends EffectfulLogging {

  implicit val rijksdataSearchResponseEntityDecoder: EntityDecoder[IO, CollectionResponse] = jsonOf

  def apply(client: Client[IO], baseUri: Uri, apiKey: String): RijksdataClient = {
    new RijksdataClient {
      override def getObjectWithTechnique: IO[CollectionResponse] = {
        val url = baseUri.withQueryParam("key", apiKey).withQueryParam("technique", "colour+woodcut")
        client.run(Request[IO](GET, url)).use {
          case response if response.status.isSuccess => response.as[CollectionResponse]
          case _ => CollectionResponse(0, List.empty).pure[IO]
        }

      }
    }
  }
}
