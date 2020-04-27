package com.michelle.rijksdata

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    RijksdataServer.stream[IO].compile.drain.as(ExitCode.Success)
}