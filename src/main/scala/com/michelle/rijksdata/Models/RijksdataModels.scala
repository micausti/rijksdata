package com.michelle.rijksdata.Models

import io.circe.Decoder
import io.circe.generic.auto
import io.circe.generic.semiauto.deriveDecoder

case class CollectionResponse(elapsedMilliseconds: Int, count: Int, artObjects: List[ArtObject])
case class ArtObject(title: String, principalOrFirstMaker: String)

object CollectionResponse {
  implicit val artObjectDecoder: Decoder[ArtObject] = deriveDecoder
  implicit val collectionResponseDecoder: Decoder[CollectionResponse] =
    deriveDecoder[CollectionResponse]
}
