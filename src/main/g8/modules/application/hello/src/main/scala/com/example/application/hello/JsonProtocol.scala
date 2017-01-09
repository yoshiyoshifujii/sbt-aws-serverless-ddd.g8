package com.example.application.hello

import com.example.domain.account.AccountId
import com.example.infrastructure.lambda.exception.InternalServerError
import spray.json._

object JsonProtocol extends DefaultJsonProtocol {

  implicit object StageVariablesJsonFormat extends RootJsonFormat[StageVariables] {

    override def read(json: JsValue): StageVariables = {
      json.asJsObject
        .getFields(
          "env",
          "region") match {
        case Seq(
        JsString(env),
        JsString(region)
        ) =>
          StageVariables(
            env     = env,
            region  = region)
        case _ =>
          throw InternalServerError()
      }
    }

    override def write(obj: StageVariables): JsValue = ???

  }

  implicit object AuthorizerJsonFormat extends RootJsonFormat[Authorizer] {

    override def read(json: JsValue): Authorizer = {
      json.asJsObject
        .getFields("account_id") match {
        case Seq(JsString(accountId)) =>
          Authorizer(AccountId(accountId))
        case _ =>
          throw InternalServerError()
      }
    }

    override def write(obj: Authorizer): JsValue = ???

  }
}
