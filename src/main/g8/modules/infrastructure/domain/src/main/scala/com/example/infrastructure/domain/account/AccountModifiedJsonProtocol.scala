package com.example.infrastructure.domain.account

import com.example.domain.account.{AccountId, AccountModified}
import spray.json._

object AccountModifiedJsonProtocol extends DefaultJsonProtocol {

  implicit object AccountModifiedJsonFormat extends RootJsonFormat[AccountModified] {

    override def read(json: JsValue): AccountModified = {
      json.asJsObject
        .getFields("account_id", "email", "name") match {
        case Seq(JsString(aid), JsString(e), JsString(n)) =>
          AccountModified(AccountId(aid), e, n)
        case _ =>
          throw new RuntimeException()
      }
    }

    override def write(obj: AccountModified): JsValue =
      JsObject(
        "account_id" -> JsString(obj.accountId.value),
        "email" -> JsString(obj.email),
        "name" -> JsString(obj.name)
      )
  }
}
