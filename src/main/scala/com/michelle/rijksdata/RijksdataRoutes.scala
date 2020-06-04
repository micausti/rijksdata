package com.michelle.rijksdata

import cats.effect.{IO, Sync}
import cats.implicits._
import org.http4s.{HttpRoutes, Method, Request}
import org.http4s.dsl.Http4sDsl
import unused.{Collection, HelloWorld, ImageClient}


object RijksdataRoutes {

  def itemDetails[Sync](R: Rijksdatas): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "item" =>
        for {
          details <- R.get
          resp <- Ok(details)
        } yield resp
    }
  }

  def detailRoutes[Sync](D: Details): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "details" =>
        for {
          details <- D.get
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

  def helloWorldRoutes[Sync](H: HelloWorld): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }

}