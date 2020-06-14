package com.michelle.rijksdata

import cats.effect.IO
import com.michelle.rijksdata.DetailsClient.ObjectNumber
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl


object RijksdataRoutes {

  def detailRoutes[Sync](D: DetailsClient): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "details" / item =>
        for {
          details <- D.get(ObjectNumber(item))
          resp <- Ok(details)
        } yield resp
    }
  }

  def jokeRoutes[Sync](J: Jokes): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }
}