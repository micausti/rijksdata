package com.michelle.rijksdata.Models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class AICSearchResponse(data: List[ApiLink])
case class ApiLink(api_link: String)

object AICSearchResponse {
  implicit val AICSearchResponseDecoder: Decoder[AICSearchResponse] = deriveDecoder[AICSearchResponse]
  implicit val apiLinkDecoder: Decoder[ApiLink] = deriveDecoder[ApiLink]
}
