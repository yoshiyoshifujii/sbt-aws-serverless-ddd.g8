package com.example.infrastructure.lambda.consumer.model

sealed abstract class KinesisFailure(message: String, cause: Option[Throwable])

case class RetryKinesisFailure(message: String, cause: Option[Throwable] = None) extends KinesisFailure(message, cause)

case class SkipKinesisFailure(message: String, cause: Option[Throwable] = None) extends KinesisFailure(message, cause)


