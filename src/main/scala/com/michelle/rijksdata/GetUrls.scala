package com.michelle.rijksdata

import cats.effect.{Clock, IO}
import cats.implicits.catsSyntaxFlatMapOps
import com.michelle.rijksdata.Clients.AICClient.logger
import com.michelle.rijksdata.Clients.{AICClient, MetClient, RijksdataClient}

class GetUrls(aicClient: AICClient, metClient: MetClient, rijksdataClient: RijksdataClient)(implicit clock: Clock[IO]) {

  def getFilesForAIC: IO[List[String]] =
    for {
      searchResponse <- aicClient.getSearchResult
      items          <- aicClient.getItemResult(searchResponse)
      _ <- logger.info(s"finished getting items")
      urls           <- aicClient.getImageUrls(items)
     _ <- logger.info(s"finished getting urls")
    } yield urls

  def getFilesForMet: IO[List[String]] =
    for {
      searchResponse <- metClient.getSearchResult
      _              <- logger.info(s"finished searching")
      items          <- metClient.getObjectResult(searchResponse)
      _ <- logger.info(s"met objects $items")
      urls           <- IO(items.map(_.primaryImage))
    _ <- logger.info(s"urls $urls")
    } yield urls

  def getFilesForRijks: IO[List[String]] = {
    val collectionResponse = rijksdataClient.getObjectWithTechnique
    collectionResponse.map(_.artObjects.map(_.webImage.url))
  }
}
