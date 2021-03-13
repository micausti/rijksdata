package com.michelle.rijksdata.Models

import io.circe.generic.semiauto.deriveDecoder
import io.circe.Decoder

case class WebImage(url: String)
case class ArtObject(principalOrFirstMaker: String, longTitle: String, webImage: WebImage, productionPlaces: List[String])
case class CollectionResponse(count: Int, artObjects: List[ArtObject])

object CollectionResponse {
  implicit val webImageDecoder: Decoder[WebImage] = deriveDecoder[WebImage]
  implicit val decodeArtObject: Decoder[ArtObject] = deriveDecoder[ArtObject]
  implicit val decodeCollectionResponse: Decoder[CollectionResponse] = deriveDecoder[CollectionResponse]
}
