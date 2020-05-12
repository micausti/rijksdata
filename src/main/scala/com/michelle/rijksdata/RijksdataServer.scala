package com.michelle.rijksdata

import cats.effect.{ConcurrentEffect, ContextShift, IO, Timer}
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
      helloWorldAlg = HelloWorld.impl[IO[HelloWorld]]
      jokeAlg = Jokes.impl[IO[Jokes]](client)
      collectionAlg = Collection.impl[IO[Collection]](client)
      rembrandtAlg = RijksCollections.impl[IO[RijksCollections]](client)

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = (
        RijksdataRoutes.helloWorldRoutes[IO[HelloWorld]](helloWorldAlg) <+>
        RijksdataRoutes.jokeRoutes[IO[Jokes]](jokeAlg) <+>
          RijksdataRoutes.collectionRoutes[IO[Collection]](collectionAlg) <+>
        RijksdataRoutes.rembrandtRoutes[IO[RijksCollections]](rembrandtAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}