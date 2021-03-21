package com.michelle.rijksdata.Models

trait JobStatus

object JobStatus {

  case object Success extends JobStatus

  case object Failed extends JobStatus

}
