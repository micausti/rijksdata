package com.michelle.rijksdata.Clients

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import com.michelle.rijksdata.EffectfulLogging
import com.michelle.rijksdata.Models.Rijksdata.RijksdataObjectDetail
import com.michelle.rijksdata.Models.RijksdataSearchResponse
import io.circe.generic.semiauto._
import io.circe.optics.JsonPath.root
import io.circe.{Decoder, Encoder, Json}
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.client.impl.EntityRequestGenerator
import org.http4s.implicits._
import org.http4s.{EntityDecoder, EntityEncoder, Request, _}
import org.http4s.dsl.io.GET

trait RijksdataClient {
  def getObjectWithTechnique: IO[RijksdataSearchResponse]
}

object RijksdataClient extends EffectfulLogging {

  implicit val rijksdataSearchResponseEntityDecoder: EntityDecoder[IO, RijksdataSearchResponse] = jsonOf

  def apply(client: Client[IO], baseUri: Uri, apiKey: String): RijksdataClient = {
    new RijksdataClient {
      override def getObjectWithTechnique: IO[RijksdataSearchResponse] = {
        val url = baseUri.withQueryParam("key", apiKey).withQueryParam("technique", "colour+woodcut")
        client.run(Request[IO](GET, url)).use {
          case response if response.status.isSuccess => response.as[RijksdataSearchResponse]
          case _ => RijksdataSearchResponse(List.empty).pure[IO]
        }

      }
    }
  }
}
