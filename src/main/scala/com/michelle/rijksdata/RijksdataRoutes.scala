package com.michelle.rijksdata

import cats.effect.{IO, Sync}
import cats.implicits._
import org.http4s.{HttpRoutes, Method, Request}
import org.http4s.dsl.Http4sDsl
import unused.{Collection, HelloWorld, ImageClient, Jokes}


object RijksdataRoutes {

  def imageDetails[Sync](I: Rijksdata): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / itemId / "imageDetails" =>
        for {
          imageDetails <- I.get(Rijksdata.ItemId(itemId))
          resp <- Ok(imageDetails)
        } yield resp
    }
  }

  def imageDetails2[Sync](I: Rijksdata): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / itemId / "imageDetails" =>
        for {
          imageDetails <- I.get(Rijksdata.ItemId(itemId))
          resp <- Ok(imageDetails)
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