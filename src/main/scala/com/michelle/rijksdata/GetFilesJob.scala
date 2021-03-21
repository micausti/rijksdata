package com.michelle.rijksdata

import cats.effect.IO
import com.michelle.rijksdata.Models.JobStatus

case class GetFilesJob() extends EffectfulLogging {

  def run(): IO[JobStatus] = ???
}
