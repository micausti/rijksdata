package com.michelle.rijksdata.Models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class AICSearchResponse(pagination: Pagination, links: List[ApiLink])
case class ApiLink(api_link: String)
case class Pagination(total: Int)

object AICSearchResponse {
  implicit val AICSearchResponseDecoder: Decoder[AICSearchResponse] = deriveDecoder[AICSearchResponse]
  implicit val apiLinkDecoder: Decoder[ApiLink] = deriveDecoder[ApiLink]
  implicit val paginationDecoder: Decoder[Pagination] = deriveDecoder[Pagination]
}
