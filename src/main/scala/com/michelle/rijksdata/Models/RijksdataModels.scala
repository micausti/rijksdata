package com.michelle.rijksdata.Models

import io.circe.generic.semiauto.deriveDecoder
import io.circe.Decoder

case class ArtObject(principalOrFirstMaker: String, longTitle: String)
case class CollectionResponse(count: Int, artObjects: List[ArtObject])

object CollectionResponse {
  implicit val decodeArtObject: Decoder[ArtObject] = deriveDecoder[ArtObject]
  implicit val decodeCollectionResponse: Decoder[CollectionResponse] = deriveDecoder[CollectionResponse]
}
