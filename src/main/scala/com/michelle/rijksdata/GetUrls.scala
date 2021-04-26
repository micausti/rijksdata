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
      urls           <- IO(items.map(_.config.iiif_url))
    } yield urls

  def getFilesForMet: IO[List[String]] =
    for {
      searchResponse <- metClient.getSearchResult
      _              <- logger.info(s"finished searching")
      items          <- metClient.getObjectResult(searchResponse)
      urls           <- IO(items.map(_.primaryImage))
    } yield urls

  def getFilesForRijks: IO[List[String]] = {
    val collectionResponse = rijksdataClient.getObjectWithTechnique
    collectionResponse.map(_.artObjects.map(_.webImage.url))
  }
}
