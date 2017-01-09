package $package$.application.accountmodified

import $package$.domain.account.AccountModified
import $package$.infrastructure.lambda.consumer.handler.BaseKinesisHandler
import $package$.infrastructure.lambda.consumer.model.KinesisEvent.KinesisEventRecord
import $package$.infrastructure.lambda.consumer.model.{RetryKinesisFailure, SkipKinesisFailure}
import spray.json._

trait Base extends BaseKinesisHandler {

  override type Input = AccountModified
  override type Output = Unit

  override protected def convert(event: KinesisEventRecord) = {
    import $package$.infrastructure.domain.account.AccountModifiedJsonProtocol._
    try {
      Right(JsonParser(event.kinesis.data.array)
        .asJsObject.convertTo[AccountModified])
    } catch {
      case e: java.io.IOException =>
        throw e
      case e: Throwable =>
        Left(SkipKinesisFailure(e.getMessage, Some(e)))
    }
  }

  override protected def convertFail(error: SkipKinesisFailure) =
    error.cause.fold(println(error.message))(c => println(error.message, c))

  override protected def handle(input: Input) = {
    println("create ElasticSearch index")
    Right()
  }

  override protected def success(input: Input, output: Output) =
    println(output.toString)

  override protected def handleFail(input: Input, error: SkipKinesisFailure) =
    error.cause.fold(println(error.message))(c => println(error.message, c))

  override protected def handleFail(input: Input, error: RetryKinesisFailure) =
    error.cause.fold {
      println(error.message)
      throw new java.io.IOException(error.message)
    } { c =>
      println(error.message, c)
      throw new java.io.IOException(error.message, c)
    }

  override protected def fail(e: Throwable): Unit = e match {
    case io: java.io.IOException => throw io
    case other: Throwable => println(other.getMessage, other)
  }

}
