package com.example.infrastructure.lambda.rest

import java.util.Base64

import spray.json._

package object error {
  type Reason = String
  type Item = String

  trait ErrorDetailBase {
    val reason: Reason
    def toJsonObject: JsObject
  }
  case class ErrorDetail(reason: Reason) extends ErrorDetailBase {
    override def toJsonObject: JsObject = {
      JsObject(
        "reason" -> JsString(reason)
      )
    }
  }
  case class ErrorItemDetail(reason: Reason, item: Item) extends ErrorDetailBase {
    override def toJsonObject: JsObject = {
      JsObject(
        "reason" -> JsString(reason),
        "item" -> JsString(item)
      )
    }
  }

  case class ErrorDetails(value: ErrorDetailBase*) {
    def toJsArray: JsArray = {
      JsArray(value.map(_.toJsonObject): _*)
    }
  }

  type ErrorCode = String

  trait ErrorBase {
    val CharsetName = "utf-8"
    val code: ErrorCode
    def toJsonStringified: String
    def toBase64Encode: String = {
      Base64.getEncoder.encodeToString(toJsonStringified.getBytes(CharsetName))
    }
  }
  case class Error(code: ErrorCode) extends ErrorBase {
    override def toJsonStringified: String = {
      JsObject(
        "code" -> JsString(code)
      ).compactPrint
    }
  }
  case class ErrorWithDetails(code: ErrorCode, details: ErrorDetails) extends ErrorBase {
    override def toJsonStringified: String = {
      JsObject(
        "code" -> JsString(code),
        "details" -> details.toJsArray
      ).compactPrint
    }
  }

  class ErrorMessage(val statusCode: HttpStatusCode, val error: String) {
    import ErrorMessage._

    lazy val jsObject = JsObject(
      KeyStatusCode -> JsNumber(statusCode),
      KeyError -> JsString(error))

    def jsonCompactPrint: String = jsObject.compactPrint
    // JavaBeans依存
    def getError(): String = error
  }

  object ErrorMessage {
    val KeyStatusCode = "statusCode"
    val KeyError = "error"

    def apply(statusCode: HttpStatusCode,
              error: ErrorBase = null): ErrorMessage = {
      new ErrorMessage(statusCode, if (error == null) "" else error.toBase64Encode)
    }
  }

  object ErrorMessageJsonProtocol extends DefaultJsonProtocol {
    import ErrorMessage._
    implicit object ErrorMessageJsonFormat extends RootJsonFormat[ErrorMessage] {
      override def read(value: JsValue): ErrorMessage = {
        value.asJsObject.getFields("statusCode", "error") match {
          case Seq(JsNumber(statusCode), JsString(error)) =>
            new ErrorMessage(statusCode.toInt, error)
          case _ => throw DeserializationException("ErrorMessage expected.")
        }
      }

      override def write(obj: ErrorMessage): JsValue = JsObject(
        KeyStatusCode -> JsNumber(obj.statusCode),
        KeyError -> JsString(obj.error)
      )
    }
  }
}
