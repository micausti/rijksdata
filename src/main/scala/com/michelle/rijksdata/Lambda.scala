package com.michelle.rijksdata

import cats.effect.{Clock, ContextShift, Fiber, IO, Resource, Timer}
import com.michelle.rijksdata.Clients.{AICClient, MetClient, RijksdataClient}
import com.michelle.rijksdata.Models.JobStatus
import org.http4s.client.blaze.{BlazeClientBuilder, BlazeClientConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal
import cats.implicits._
import org.http4s.client.blaze.ParserMode.Lenient

class Lambda extends EffectfulLogging {

  def handler(): Unit = {
    implicit val contextShift: ContextShift[IO] = IO.contextShift(global)
    implicit val timer: Timer[IO] = IO.timer(global)

    for {
      _ <- logger.info(s"starting lambda")
      _ <- buildResources.use(getFiles)
    } yield ()
  }.unsafeRunSync()

  def buildResources(
      implicit contextShift: ContextShift[IO]): Resource[IO, Resources] =
    for {
      config <- Resource.liftF(Config.loadConfig)
      http4sClient <- BlazeClientBuilder[IO](global).withParserMode(Lenient).resource
      rijksdataClient = RijksdataClient(http4sClient,
                                        config.rijksdataBaseUri,
                                        config.rijksdataApiKey)
      aicClient = AICClient(http4sClient, config.aicBaseUri)
    metClient = MetClient(http4sClient, config.metBaseUri)
    } yield Resources(config, rijksdataClient, aicClient, metClient)

  private implicit class LogErrors(iou: IO[JobStatus]) {
    def handleErrors(jobName: String): IO[JobStatus] =
      iou.recoverWith {
          case NonFatal(t) => logger.error(t)(s"Error running job $jobName").as(JobStatus.Failed)
        }
  }


  def getFiles(resources:Resources)(implicit clock: Clock[IO], contextShift: ContextShift[IO]): IO[List[String]] = {
    val getFilesBuilder = new GetUrls(resources.AICClient, resources.metClient, resources.rijksdataClient)

    for {
      _ <- logger.info(s"aic")
    aic <- getFilesBuilder.getFilesForAIC
      _ <- logger.info(s"rijks")
      rijks <- getFilesBuilder.getFilesForRijks
      _ <- logger.info(s"met")
    met <- getFilesBuilder.getFilesForMet
    compiledUrls <- IO(rijks ++ aic ++ met)
    } yield compiledUrls
  }
}



case class Resources(config: Config,
                     rijksdataClient: RijksdataClient,
                     AICClient: AICClient, metClient: MetClient)
