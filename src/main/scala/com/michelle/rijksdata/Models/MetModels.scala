package com.michelle.rijksdata.Models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class MetSearchResult(objectIDs: List[Int])
case class MetObjectResult(primaryImage: String)

object MetSearchResult {
  implicit val metSearchResultDecoder: Decoder[MetSearchResult] = deriveDecoder
}

object MetObjectResult {
  implicit val metObjectResultDecoder: Decoder[MetObjectResult] = deriveDecoder
}
