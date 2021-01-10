package com.michelle.rijksdata.lambdaProcessing

sealed trait ConsumeAction

final case class Requeue(message: String, throwable: Option[Throwable] = None) extends ConsumeAction

final case class DeadLetter(message: String, throwable: Option[Throwable] = None) extends ConsumeAction

case object Ack extends ConsumeAction
