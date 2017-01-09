package com.example

import java.io.{BufferedInputStream, InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import spray.json._

class Auth extends RequestStreamHandler {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val bis = new BufferedInputStream(input)
    val bytes = Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
    JsonParser(bytes).asJsObject.getFields("methodArn") match {
      case Seq(JsString(methodArn)) =>
        output.write(JsObject(
          "principalId" -> JsString(
            JsObject("account_id" -> JsString("xxx")).compactPrint
          ),
          "policyDocument" -> JsObject(
            "Statement" -> JsArray(
              JsObject(
                "Action" -> JsString("execute-api:Invoke"),
                "Effect" -> JsString("Allow"),
                "Resource" -> JsString(methodArn)
              )
            ))
        ).compactPrint.getBytes("utf-8"))
      case _ =>
    }
  }
}
