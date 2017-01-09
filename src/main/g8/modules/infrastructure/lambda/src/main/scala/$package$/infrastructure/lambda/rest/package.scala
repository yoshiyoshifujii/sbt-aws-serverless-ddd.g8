package $package$.infrastructure.lambda

package object rest {
  type HttpStatusCode = Int

  /**
    * http://www.hcn.zaq.ne.jp/___/WEB/RFC723X-ja.html
    */
  object HttpStatusCode {
    val Continue: HttpStatusCode = 100
    val SwitchingProtocols: HttpStatusCode = 101
    val Processing: HttpStatusCode = 102
    val OK: HttpStatusCode = 200
    val Created: HttpStatusCode = 201
    val Accepted: HttpStatusCode = 202
    val Non_AuthoritativeInformation: HttpStatusCode = 203
    val NoContent: HttpStatusCode = 204
    val ResetContent: HttpStatusCode = 205
    val PartialContent: HttpStatusCode = 206
    val Multi_Status: HttpStatusCode = 207
    val AlreadyReported: HttpStatusCode = 208
    val IM_Used: HttpStatusCode = 226
    val MultipleChoices: HttpStatusCode = 300
    val MovedPermanently: HttpStatusCode = 301
    val MovedTemporarily: HttpStatusCode = 302
    val SeeOther: HttpStatusCode = 303
    val NotModified: HttpStatusCode = 304
    val UseProxy: HttpStatusCode = 305
    val Unused: HttpStatusCode = 306
    val TemporaryRedirect: HttpStatusCode = 307
    val PermanentRedirect: HttpStatusCode = 308
    val BadRequest: HttpStatusCode = 400
    val Unauthorized: HttpStatusCode = 401
    val PaymentRequired: HttpStatusCode = 402
    val Forbidden: HttpStatusCode = 403
    val NotFound: HttpStatusCode = 404
    val MethodNotAllowed: HttpStatusCode = 405
    val NotAcceptable: HttpStatusCode = 406
    val ProxyAuthenticationRequired: HttpStatusCode = 407
    val RequestTimeout: HttpStatusCode = 408
    val Conflict: HttpStatusCode = 409
    val Gone: HttpStatusCode = 410
    val LengthRequired: HttpStatusCode = 411
    val PreconditionFailed: HttpStatusCode = 412
    val RequestEntityTooLarge: HttpStatusCode = 413
    val RequestURITooLarge: HttpStatusCode = 414
    val UnsupportedMediaType: HttpStatusCode = 415
    val RequestedRangeNotSatisfiable: HttpStatusCode = 416
    val ExpectationFailed: HttpStatusCode = 417
    val ImTeapot: HttpStatusCode = 418
    val UnprocessableEntity: HttpStatusCode = 422
    val Locked: HttpStatusCode = 423
    val FailedDependency: HttpStatusCode = 424
    val UnorderedCollection: HttpStatusCode = 425
    val UpgradeRequired: HttpStatusCode = 426
    val PreconditionRequired: HttpStatusCode = 428
    val TooManyRequests: HttpStatusCode = 429
    val RequestHeaderFieldsTooLarge: HttpStatusCode = 431
    val ReportLegalObstacles: HttpStatusCode = 451
    val InternalServerError: HttpStatusCode = 500
    val NotImplemented: HttpStatusCode = 501
    val BadGateway: HttpStatusCode = 502
    val ServiceUnavailable: HttpStatusCode = 503
    val GatewayTimeout: HttpStatusCode = 504
    val HTTPVersionNotSupported: HttpStatusCode = 505
    val VariantAlsoNegotiates: HttpStatusCode = 506
    val InsufficientStorage: HttpStatusCode = 507
    val BandwidthLimitExceeded: HttpStatusCode = 509
    val NotExtended: HttpStatusCode = 510
    val NetworkAuthenticationRequired: HttpStatusCode = 511
  }
}
