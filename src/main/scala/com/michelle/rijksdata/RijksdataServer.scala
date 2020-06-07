package com.michelle.rijksdata

import cats.effect.{ContextShift, IO, Timer}
import cats.implicits._
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object RijksdataServer {

  def stream[ConcurrentEffect[IO]](implicit T: Timer[IO], C: ContextShift[IO]): Stream[IO, Nothing] = {
    for {
      client <- BlazeClientBuilder[IO](global).stream
      detailsAlg = Details.impl[IO[Details]](client)
      jokeAlg = Jokes.impl[IO[Jokes]](client)
      httpApp = (RijksdataRoutes.jokeRoutes[IO[Jokes]](jokeAlg) <+>
        RijksdataRoutes.detailRoutes[IO[Details]](detailsAlg)).orNotFound
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}