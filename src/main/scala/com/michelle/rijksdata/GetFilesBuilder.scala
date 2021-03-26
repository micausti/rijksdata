package com.michelle.rijksdata

import cats.effect.{Clock, IO}
import com.michelle.rijksdata.Clients.{AICClient, MetClient, RijksdataClient}
import com.michelle.rijksdata.Models.{AICItemResult, CollectionResponse, MetObjectResult}

class GetFilesBuilder(aicClient: AICClient, metClient: MetClient, rijksdataClient: RijksdataClient)(implicit clock: Clock[IO]) {

  def getFilesForAIC: IO[List[AICItemResult]] = {
    for {
      searchResponse <- aicClient.getSearchResult
    items <- aicClient.getItemResult(searchResponse)
    } yield items
  }

  def getFilesForMet: IO[List[MetObjectResult]] = {
    for {
    searchResponse <- metClient.getSearchResult
    items <- metClient.getObjectResult(searchResponse)
    } yield items
  }

  def getFilesForRijks: IO[CollectionResponse] = rijksdataClient.getObjectWithTechnique
}
