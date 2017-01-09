package $package$.application.hello

import $package$.domain.account.{AccountEventPublisher, AccountRepository}
import $package$.infrastructure.lambda.exception.InternalServerError
import $package$.infrastructure.lambda.handler.BaseStreamHandler
import spray.json._

import scala.util.Try

trait Base extends BaseStreamHandler {

  val accountRepository: AccountRepository

  val accountEventPublisher: AccountEventPublisher

  override type Input = Request

  override def convert(bytes: Array[Byte]): Try[Input] = Try {
    import JsonProtocol._
    JsonParser(bytes).asJsObject
      .getFields("stage-variables", "authorizer") match {
      case Seq(sv @ JsObject(_), au @ JsObject(_)) =>
        Request(
          stageVariables = sv.convertTo[StageVariables],
          authorizer = au.convertTo[Authorizer]
        )
      case _ =>
        throw InternalServerError()
    }
  }

  override def handle(input: Input): Try[String] = Try {
    JsObject(
      "message" -> JsString("hello world!!")
    ).compactPrint
  }
}
