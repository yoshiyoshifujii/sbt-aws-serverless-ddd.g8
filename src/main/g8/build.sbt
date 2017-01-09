import Dependencies._
import RequestTemplates._
import com.github.yoshiyoshifujii.aws.apigateway._
import ResponseTemplatePatterns._

lazy val accountId = sys.props.getOrElse("AWS_ACCOUNT_ID", "")
lazy val regionName = sys.props.getOrElse("AWS_REGION", "")
lazy val restApiId = sys.props.getOrElse("AWS_REST_API_ID", "")
lazy val roleArn = sys.props.getOrElse("AWS_ROLE_ARN", "")
lazy val bucketName = sys.props.getOrElse("AWS_BUCKET_NAME", "")
lazy val authKey = sys.props.getOrElse("AUTH_KEY", "")

val commonSettings = Seq(
  version := "$version$",
  scalaVersion := "2.12.1",
  organization := "$organization$",
  libraryDependencies ++= rootDeps
)

val assemblySettings = Seq(
  assemblyMergeStrategy in assembly := {
    case "application.conf" => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  assemblyJarName in assembly := s"\${name.value}-\${version.value}.jar",
  publishArtifact in (Compile, packageBin) := false,
  publishArtifact in (Compile, packageSrc) := false,
  publishArtifact in (Compile, packageDoc) := false
)

val awsSettings = Seq(
  awsRegion := regionName,
  awsAccountId := accountId
)

val apiGatewaySettings = awsSettings ++ Seq(
  awsApiGatewayRestApiId := restApiId,
  awsApiGatewayYAMLFile := file("./swagger.yaml"),
  awsApiGatewayResourceUriLambdaAlias := "\${stageVariables.env}",
  awsApiGatewayStages := Seq(
    "dev" -> Some("development stage"),
    "test" -> Some("test stage"),
    "v1" -> Some("v1 stage")
  ),
  awsApiGatewayStageVariables := Map(
    "dev" -> Map("env" -> "dev", "region" -> regionName),
    "test" -> Map("env" -> "test", "region" -> regionName),
    "v1" -> Map("env" -> "production", "region" -> regionName)
  )
)

val lambdaSettings = apiGatewaySettings ++ Seq(
  awsLambdaFunctionName := s"\${name.value}",
  awsLambdaDescription := "sample-serverless",
  awsLambdaRole := roleArn,
  awsLambdaTimeout := 15,
  awsLambdaMemorySize := 1536,
  awsLambdaS3Bucket := bucketName,
  awsLambdaDeployDescription := s"\${version.value}",
  awsLambdaAliasNames := Seq(
    "test", "production"
  )
)

lazy val root = (project in file(".")).
  enablePlugins(AWSApiGatewayPlugin).
  aggregate(appHello, appAccountModified).
  settings(commonSettings: _*).
  settings(apiGatewaySettings: _*).
  settings(
    name := "$name$"
  )

lazy val domain = (project in file("./modules/domain")).
  settings(commonSettings: _*).
  settings(
    name := "$name$-domain",
    libraryDependencies ++= domainDeps
  )

lazy val infraLambda = (project in file("./modules/infrastructure/lambda")).
  settings(commonSettings: _*).
  settings(
    name := "$name$-infrastructure-lambda",
    libraryDependencies ++= infraLambdaDeps
  )

lazy val infraLambdaConsumer = (project in file("./modules/infrastructure/lambdaconsumer")).
  settings(commonSettings: _*).
  settings(
    name := "$name$-infrastructure-lambda-consumer",
    libraryDependencies ++= infraLambdaConsumerDeps
  )

lazy val infraDomain = (project in file("./modules/infrastructure/domain")).
  dependsOn(domain).
  settings(commonSettings: _*).
  settings(
    name := "$name$-infrastructure-domain",
    libraryDependencies ++= infraDomainDeps
  )

lazy val infraDynamo = (project in file("./modules/infrastructure/dynamodb")).
  dependsOn(infraDomain).
  settings(commonSettings: _*).
  settings(
    name := "$name$-infrastructure-dynamodb",
    libraryDependencies ++= infraDynamoDeps
  )

lazy val infraS3 = (project in file("./modules/infrastructure/s3")).
  dependsOn(infraDomain).
  settings(commonSettings: _*).
  settings(
    name := "$name$-infrastructure-s3",
    libraryDependencies ++= infraS3Deps
  )

lazy val infraKinesis = (project in file("./modules/infrastructure/kinesis")).
  dependsOn(infraDomain).
  settings(commonSettings: _*).
  settings(
    name := "$name$-infrastructure-kinesis",
    libraryDependencies ++= infraKinesisDeps
  )

lazy val infraElasticSearch = (project in file("./modules/infrastructure/elasticsearch")).
  dependsOn(infraDomain).
  settings(commonSettings: _*).
  settings(
    name := "$name$-infrastructure-elasticsearch",
    libraryDependencies ++= infraElasticSearchDeps
  )

lazy val auth = (project in file("./modules/auth")).
  enablePlugins(AWSCustomAuthorizerPlugin).
  settings(commonSettings: _*).
  settings(assemblySettings: _*).
  settings(lambdaSettings: _*).
  settings(
    name := "$name$-auth",
    libraryDependencies ++= authDeps,
    awsLambdaHandler := "$package$.Auth::handleRequest",
    awsAuthorizerName := "$name$-auth",
    awsIdentitySourceHeaderName := "Authorization",
    awsAuthorizerResultTtlInSeconds := 1800
  )

lazy val appHello = (project in file("./modules/application/hello")).
  dependsOn(infraLambda, infraDynamo, infraS3, infraKinesis).
  enablePlugins(AWSServerlessPlugin).
  settings(commonSettings: _*).
  settings(assemblySettings: _*).
  settings(lambdaSettings: _*).
  settings(
    name := "$name$-app-hello",
    libraryDependencies ++= appHelloDeps,
    awsLambdaHandler := "$package$.application.hello.App::handleRequest",
    awsApiGatewayResourcePath := "/hellos",
    awsApiGatewayResourceHttpMethod := "GET",
    awsApiGatewayIntegrationRequestTemplates := Seq(
      "application/json" -> AllParameters
    ),
    awsApiGatewayIntegrationResponseTemplates := ResponseTemplates(
      Response200,
      Response408,
      Response500
    ),
    awsMethodAuthorizerName := "$name$-auth",
    awsTestHeaders := Seq("Authorization" -> authKey),
    awsTestSuccessStatusCode := 200
  )

lazy val appAccountModified = (project in file("./modules/application/accountmodified")).
  dependsOn(infraLambdaConsumer, infraDomain).
  enablePlugins(AWSLambdaTriggerKinesisStreamPlugin).
  settings(commonSettings: _*).
  settings(assemblySettings: _*).
  settings(lambdaSettings: _*).
  settings(
    name := "$name$-app-account-modified",
    libraryDependencies ++= appAccountModifiedDeps,
    awsLambdaHandler := "$package$.application.accountmodified.App::recordHandler",
    eventSourceNames := Seq("account-modified")
  )

