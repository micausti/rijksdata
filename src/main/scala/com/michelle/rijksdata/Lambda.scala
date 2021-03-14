package com.michelle.rijksdata

import cats.effect.{ContextShift, IO, Resource, Timer}
import com.michelle.rijksdata.Clients.{AICClient, RijksdataClient}
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext.Implicits.global

class Lambda extends EffectfulLogging {

  def handler(): Unit = {
    implicit val contextShift: ContextShift[IO] = IO.contextShift(global)
    implicit val timer: Timer[IO] = IO.timer(global)

    for {
      _ <- logger.info(s"starting lambda")
      _ <- buildResources.use(???)
    } yield ()
  }.unsafeRunSync()

  def buildResources(
      implicit contextShift: ContextShift[IO]): Resource[IO, Resources] =
    for {
      config <- Resource.liftF(Config.loadConfig)
      http4sClient <- BlazeClientBuilder[IO](global).resource
      rijksdataClient = RijksdataClient(http4sClient,
                                        config.rijksdataBaseUri,
                                        config.rijksdataApiKey)
      aicClient = AICClient(http4sClient, config.aicBaseUri)
    } yield Resources(config, rijksdataClient, aicClient)

}
case class Resources(config: Config,
                     rijksdataClient: RijksdataClient,
                     AICClient: AICClient)
