package com.michelle.rijksdata

import cats.effect.IO
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

/**
 * log4cats provides a logging interface that is abstracted within an effect type `F`. So
 * no more manually wrapping all your logging calls in `IO`!
 */
trait EffectfulLogging {
  lazy val logger: Logger[IO] = Slf4jLogger.getLoggerFromClass(this.getClass)
}

