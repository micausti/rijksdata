package com.michelle.rijksdata

import cats.effect.IO
import org.http4s.Uri

case class Config(rijksdataBaseUri: Uri,
                  rijksdataApiKey: String,
                  aicBaseUri: Uri,
                  metBaseUri: Uri) {}


object Config {

  def loadConfig: IO[Config] =
    for {
      config <- IO(sys.env.getOrElse("ENVIRONMENT", "local")).map {
        case "prd" =>
          Config(
            Uri.unsafeFromString("https://www.rijksmuseum.nl/api/en/collection"),
            "apiKey",
            Uri.unsafeFromString("https://api.artic.edu/api/v1"),
            Uri.unsafeFromString("https://collectionapi.metmuseum.org")
          )
        case "local" =>
          Config(
            Uri.unsafeFromString("https://www.rijksmuseum.nl/api/en/collection"),
            "apiKey",
            Uri.unsafeFromString("https://api.artic.edu/api/v1"),
            Uri.unsafeFromString("https://collectionapi.metmuseum.org")
          )
      }
    } yield config
}
