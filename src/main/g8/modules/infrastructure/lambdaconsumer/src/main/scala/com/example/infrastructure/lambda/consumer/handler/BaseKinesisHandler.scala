package com.example.infrastructure.lambda.consumer.handler

import com.example.infrastructure.lambda.consumer.model.KinesisEvent._
import com.example.infrastructure.lambda.consumer.model.{KinesisFailure, RetryKinesisFailure, SkipKinesisFailure}

import scala.util.{Failure, Success}

trait BaseKinesisHandler {
  type Input
  type Output

  protected def convert(event: KinesisEventRecord): Either[SkipKinesisFailure, Input]
  protected def convertFail(error: SkipKinesisFailure): Unit

  protected def handle(input: Input): Either[KinesisFailure, Output]
  protected def success(input: Input, output: Output): Unit
  protected def handleFail(input: Input, error: SkipKinesisFailure): Unit
  protected def handleFail(input: Input, error: RetryKinesisFailure): Unit
  protected def fail(e: Throwable): Unit

  @throws[java.io.IOException]
  def recordHandler(event: Object): Unit = {
    parse(event) match {
      case Failure(ex) => fail(ex)
      case Success(ke) => ke.records foreach {
        r => convert(r).fold(
          e => convertFail(e),
          i => handle(i) match {
            case Left(e: SkipKinesisFailure) => handleFail(i, e)
            case Left(e: RetryKinesisFailure) => handleFail(i, e)
            case Right(s) => success(i, s)
          }
        )
      }
    }
  }
}

