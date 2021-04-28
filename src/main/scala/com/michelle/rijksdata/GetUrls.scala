package com.michelle.rijksdata

import cats.FlatMap.ops.toAllFlatMapOps
import cats.effect.{Clock, IO}
import com.michelle.rijksdata.Clients.AICClient.logger
import com.michelle.rijksdata.Clients.{AICClient, MetClient, RijksdataClient}

class GetUrls(aicClient: AICClient, metClient: MetClient, rijksdataClient: RijksdataClient)(implicit clock: Clock[IO]) {

  def getFilesForAIC: IO[List[String]] =
    for {
      searchResponse <- aicClient.getSearchResult
      items          <- aicClient.getItemResult(searchResponse)
      _              <- logger.info(s"finished getting items")
      urls           <- IO(aicClient.prepareImageUrls(items))
      _              <- logger.info(s"urls $urls")
    } yield urls

  def getFilesForMet: IO[List[String]] =
    for {
      searchResponse <- metClient.getSearchResult
      _              <- logger.info(s"finished searching")
      items          <- metClient.getObjectResult(searchResponse)
      _              <- logger.info(s"met objects $items")
      urls           <- IO(items.map(_.primaryImage))
      _              <- logger.info(s"urls $urls")
    } yield urls

  def getFilesForRijks: IO[List[String]] =
    for {
      collectionResponse <- rijksdataClient.getObjectWithTechnique
      _                  <- logger.info(s"collection Response $collectionResponse")
      urls               <- IO(collectionResponse.artObjects.map(_.webImage.url))
      _                  <- logger.info(s"urls $urls")
    } yield urls
}
