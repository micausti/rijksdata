package com.michelle.rijksdata.dataModel

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

final case class S3Bucket(value: String)
final case class S3Key(value: String)
final case class S3Location(bucketName: S3Bucket, key: S3Key)

object S3Location {
  implicit val bucketDecoder: Decoder[S3Bucket] = Decoder.decodeString.map(S3Bucket)
  implicit val keyDecoder: Decoder[S3Key] = Decoder.decodeString.map(S3Key)
  implicit val bucketEncoder: Encoder[S3Bucket] = Encoder.encodeString.contramap(_.value)
  implicit val keyEncoder: Encoder[S3Key] = Encoder.encodeString.contramap(_.value)
  implicit val decoder = deriveDecoder[S3Location]
  implicit val encoder = deriveEncoder[S3Location]
}
