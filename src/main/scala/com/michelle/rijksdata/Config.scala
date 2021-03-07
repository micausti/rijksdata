package com.michelle.rijksdata

import cats.effect.IO
import org.http4s.Uri

case class Config(baseUri: Uri, apiKey: String) {}

object Config {

  def loadConfig: IO[Config] =
    for {
      config <- IO(sys.env.getOrElse("ENVIRONMENT", "local")).map {
        case "prd" =>
          Config(Uri(path = "https://www.rijksmuseum.nl/api/en/collection"),
                 "apiKey")
        case "local" =>
          Config(Uri(path = "https://www.rijksmuseum.nl/api/en/collection"),
                 "apiKey")
      }
    } yield config
}
