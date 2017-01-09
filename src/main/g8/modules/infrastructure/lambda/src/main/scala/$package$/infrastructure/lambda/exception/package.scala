package $package$.infrastructure.lambda

import $package$.infrastructure.lambda.rest.HttpStatusCode
import $package$.infrastructure.lambda.rest.error._

package object exception {

  trait LambdaRuntimeExceptionBase

  class LambdaRuntimeException(message: String  = null,
                               cause: Throwable = null) extends RuntimeException(message, cause)
    with LambdaRuntimeExceptionBase

  object LambdaRuntimeException extends LambdaRuntimeExceptionBase {
    def apply(statusCode: HttpStatusCode,
              error: ErrorBase = null,
              cause: Throwable = null): LambdaRuntimeException = {

      val em = ErrorMessage(statusCode, error)
      new LambdaRuntimeException(em.jsonCompactPrint, cause)
    }
  }

  object BadRequest extends LambdaRuntimeExceptionBase {
    def apply(): LambdaRuntimeException = {
      LambdaRuntimeException(HttpStatusCode.BadRequest)
    }
    def apply(code: ErrorCode, reason: Reason, item: Item): LambdaRuntimeException = {
      val error = ErrorWithDetails(
        code, ErrorDetails(ErrorItemDetail(reason, item))
      )
      LambdaRuntimeException(HttpStatusCode.BadRequest, error)
    }
  }

  object Unauthorized extends LambdaRuntimeExceptionBase {
    def apply(): LambdaRuntimeException = {
      LambdaRuntimeException(HttpStatusCode.Unauthorized)
    }
  }

  object Forbidden extends LambdaRuntimeExceptionBase {
    def apply(): LambdaRuntimeException = {
      LambdaRuntimeException(HttpStatusCode.Forbidden, Error("permission_denied"))
    }
  }

  object NotFound extends LambdaRuntimeExceptionBase {
    def apply(): LambdaRuntimeException = {
      LambdaRuntimeException(HttpStatusCode.NotFound, Error("Forbidden"))
    }
  }

  object Conflict extends LambdaRuntimeExceptionBase {
    def apply(): LambdaRuntimeException = {
      LambdaRuntimeException(HttpStatusCode.Conflict, Error("conflict"))
    }
  }

  object InternalServerError extends LambdaRuntimeExceptionBase {
    def apply(): LambdaRuntimeException = {
      LambdaRuntimeException(HttpStatusCode.InternalServerError)
    }
    def apply(e: Throwable): LambdaRuntimeException = {
      LambdaRuntimeException(HttpStatusCode.InternalServerError, cause=e)
    }
  }

}
