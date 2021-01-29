package com.michelle.rijksdata.dataModel

import com.michelle.rijksdata.lambdaProcessing.{CirceUnmarshaller, Unmarshaller}
import io.circe.{Decoder, HCursor}

import java.net.URLDecoder

final case class S3NewFileEvent(
                                 bucket: S3Bucket,
                                 key: S3Key
                               ) {

  val s3Location = S3Location(bucket, key)

}

object S3NewFileEvent {

  implicit val decoder: Decoder[S3NewFileEvent] = (c: HCursor) => {
    val s3 = c.downField("Records").downArray.downField("s3")
    for {
      bucket <- s3.downField("bucket").downField("name").as[String]
      key <- s3.downField("object").downField("key").as[String]
    } yield
      S3NewFileEvent(
        S3Bucket(bucket),
        S3Key(decodeKey(key))
      )
  }

  def decodeKey(key: String) = URLDecoder.decode(key.replace("+", " "), "UTF-8")

  implicit val unmarshaller: Unmarshaller[S3NewFileEvent] =
    CirceUnmarshaller.unmarshallFromDecodeJson[S3NewFileEvent]
}
