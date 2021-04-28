package com.michelle.rijksdata.Clients

import cats.effect.IO
import com.michelle.rijksdata.EffectfulLogging
import com.michelle.rijksdata.Models.CollectionResponse
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.{EntityDecoder, _}

trait RijksdataClient {
  def getObjectWithTechnique: IO[CollectionResponse]
}

object RijksdataClient extends EffectfulLogging {

  implicit val rijksdataSearchResponseEntityDecoder: EntityDecoder[IO, CollectionResponse] = jsonOf

  def apply(client: Client[IO], baseUri: Uri, apiKey: String): RijksdataClient =
    new RijksdataClient {
      override def getObjectWithTechnique: IO[CollectionResponse] = {
        val url     = baseUri.withQueryParam("key", "J5mQRBz3").withQueryParam("technique", "colour+woodcut")
        client.expect[CollectionResponse](url)
      }
    }
}
