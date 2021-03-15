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
            Uri(path = "https://www.rijksmuseum.nl/api/en/collection"),
            "apiKey",
            Uri(path = "https://api.artic.edu/api/v1"),
            Uri(path = "https://collectionapi.metmuseum.org")
          )
        case "local" =>
          Config(
            Uri(path = "https://www.rijksmuseum.nl/api/en/collection"),
            "apiKey",
            Uri(path = "https://api.artic.edu/api/v1"),
            Uri(path = "https://collectionapi.metmuseum.org")
          )
      }
    } yield config
}
