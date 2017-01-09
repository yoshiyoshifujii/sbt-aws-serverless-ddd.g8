package $package$.infrastructure.lambda.handler

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import $package$.infrastructure.lambda.exception.{InternalServerError, LambdaRuntimeException}

import scala.util.Try

trait BaseStreamHandler extends RequestStreamHandler {

  type Input

  def handle(input: Input): Try[String]

  def convert(bytes: Array[Byte]): Try[Input]

  private def toByteArray(input: InputStream) =
    Stream.continually(input.read).takeWhile(_ != -1).map(_.toByte).toArray

  protected def fail(bytes: Array[Byte], context: Context, throwable: Throwable): Unit = {
    throwable match {
      case e: LambdaRuntimeException => throw e
      case e @ _ => throw InternalServerError(e)
    }
  }

  @throws(classOf[java.io.IOException])
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val bytes = toByteArray(input)
    (for {
      i <- convert(bytes)
      res <- handle(i)
    } yield res).fold(
      e => fail(bytes, context, e),
      s => output.write(s.getBytes("utf-8"))
    )
  }
}

