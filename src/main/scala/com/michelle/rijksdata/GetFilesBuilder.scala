package com.michelle.rijksdata

import cats.effect.{Clock, IO}
import org.http4s.Uri

class GetFilesBuilder()(implicit clock: Clock[IO]) {

  def getFilesFor(uri: Uri): GetFilesJob = ???
}
