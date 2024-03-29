package com.michelle.rijksdata.Models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.blaze.http.Url

case class AICSearchResponse(data: List[ApiLink])
case class ApiLink(api_link: String)

object AICSearchResponse {
  implicit val AICSearchResponseDecoder: Decoder[AICSearchResponse] =
    deriveDecoder
  implicit val apiLinkDecoder: Decoder[ApiLink] = deriveDecoder
}

case class AICItemResult(data: ItemData, config: ItemConfig)
case class ItemData(image_id: String)
case class ItemConfig(iiif_url: String)

object AICItemResult {
  implicit val AICItemResultDecoder: Decoder[AICItemResult] = deriveDecoder
  implicit val itemDataDecoder: Decoder[ItemData]           = deriveDecoder
  implicit val itemConfigDecoder: Decoder[ItemConfig]       = deriveDecoder
}

case class AIC(itemResult: AICItemResult)