package com.michelle.rijksdata.Models

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import org.http4s.dsl.io.GET
import org.http4s.{EntityDecoder, Header, Request}

case class RijksdataObjectDetail(longTitle: String,
                                         principalOfFirstMaker: String,
                                         webimageURL: String,
                                         productionPlaces: String)
object RijksdataObjectDetail {
  implicit val decoder: Decoder[RijksdataObjectDetail] = deriveDecoder
}

case class RijksdataSearchHit(objectDetail: RijksdataObjectDetail)
object RijksdataSearchHit {
  implicit val decoder: Decoder[RijksdataSearchHit] = deriveDecoder
}

case class RijksdataSearchResponse(searchHits: List[RijksdataSearchHit])
object RijksdataSearchResponse {
  implicit val decoder: Decoder[RijksdataSearchResponse] = deriveDecoder
}
