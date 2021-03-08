package com.michelle.rijksdata.Models

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import com.michelle.rijksdata.Models.CollectionResponse.{ArtObject, Count}
import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import org.http4s.dsl.io.GET
import org.http4s.{EntityDecoder, Header, Request}

case class CollectionResponse(artObjects: List[ArtObject])

object CollectionResponse {
  case class Count(value: Int)
  implicit val countDecoder: Decoder[Count] = deriveDecoder

  case class WebImage(url: String)
  implicit val webImageDecoder: Decoder[WebImage] = deriveDecoder

//  case class ArtObject(longTitle: String,
//                       principalOfFirstMaker: String,
//                       webImage: WebImage,
//                       productionPlaces: String)

  case class ArtObject(longTitle: String,
                       principalOfFirstMaker: String,
                       productionPlaces: String)

  implicit val artObjectDecoder: Decoder[ArtObject] = deriveDecoder[ArtObject].at("artObjects")

  implicit val decoder: Decoder[CollectionResponse] = deriveDecoder
  }
