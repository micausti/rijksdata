package com.michelle.rijksdata.api

import cats.effect.{ContextShift, IO, Timer}
import com.michelle.rijksdata.DetailsClient
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object RijksdataServer {
//todo consider taking this out 
  def stream[ConcurrentEffect[IO]](implicit T: Timer[IO], C: ContextShift[IO]): Stream[IO, Nothing] = {
    for {
      client <- BlazeClientBuilder[IO](global).stream
      detailsAlg = DetailsClient.apply(client)
      httpApp = (RijksdataRoutes.detailRoutes[IO[DetailsClient]](detailsAlg)).orNotFound
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}