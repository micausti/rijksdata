package com.michelle.rijksdata

import cats.effect.{IO, Sync}
import cats.implicits._
import org.http4s.{HttpRoutes, Method, Request}
import org.http4s.dsl.Http4sDsl


object RijksdataRoutes {

  def jokeRoutes[Sync](J: Jokes): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO]{}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }

  def collectionRoutes[Sync](C: Collection): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO]{}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "collection" =>
        for {
          collection <- C.get
          resp <- Ok(collection)
        } yield resp
    }
  }

  def helloWorldRoutes[Sync](H: HelloWorld): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO]{}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }

  def rembrandtRoutes[Sync](R: RijksCollections): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "rembrandt" =>
        for {
          rembrandt <- R.get
          resp <- Ok(rembrandt)
        } yield resp
    }
  }
}