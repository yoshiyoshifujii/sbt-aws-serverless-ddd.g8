import com.github.yoshiyoshifujii.aws.apigateway.ResponseTemplate

object ResponseTemplatePatterns {

  private lazy val responseErrorTemplate =
    """#set (\$errorMessageObj = \$util.parseJson(\$input.path('\$.errorMessage')))
      |\$util.base64Decode(\$errorMessageObj.error)""".stripMargin

  val corsMap = Map("Access-Control-Allow-Origin" -> "'*'")

  val Response200 = ResponseTemplate("200")
  val Response204 = ResponseTemplate("204")
  val Response400 = ResponseTemplate("400", Some(""".*"statusCode":400.*"""), Map("application/json" -> responseErrorTemplate))
  val Response401 = ResponseTemplate("401", Some(""".*"statusCode":401.*"""), Map("application/json" -> responseErrorTemplate))
  val Response408 = ResponseTemplate("408", Some(""".*Task timed out.*"""))
  val Response500 = ResponseTemplate("500", Some(""".*"statusCode":500.*"""), Map("application/json" -> responseErrorTemplate))

}
