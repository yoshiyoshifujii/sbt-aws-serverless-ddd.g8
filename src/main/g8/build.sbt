import Dependencies._
import serverless._

lazy val accountId = sys.props.getOrElse("AWS_ACCOUNT_ID", "")
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

lazy val root = (project in file(".")).
  enablePlugins(ServerlessPlugin).
  aggregate(auth, appHello, appAccountModified).
  settings(commonSettings: _*).
  settings(
    name := "$name$",
    serverlessOption := {
      ServerlessOption(
        Provider(
          awsAccount = accountId,
          deploymentBucket = bucketName
        ),
        ApiGateway(
          swagger = file("./swagger.yaml"),
          restApiId = None
        ),
        Functions(
          Function(
            filePath = (assemblyOutputPath in assembly in auth).value,
            name = (name in auth).value,
            handler = "$package$.Auth::handleRequest",
            role = roleArn,
            events = Events(
              AuthorizeEvent(
                name = (name in auth).value
              )
            )
          ),
          Function(
            filePath = (assemblyOutputPath in assembly in appHello).value,
            name = (name in appHello).value,
            handler = "$package$.application.hello.App::handleRequest",
            role = roleArn,
            events = Events(
              HttpEvent(
                path = "/hellos",
                method = "GET",
                cors = true,
                authorizerName = (name in auth).value,
                invokeInput = HttpInvokeInput(
                  headers = Seq("Authorization" -> authKey)
                )
              )
            )
          ),
          Function(
            filePath = (assemblyOutputPath in assembly in appAccountModified).value,
            name = (name in appAccountModified).value,
            handler = "$package$.application.accountmodified.App::recordHandler",
            role = roleArn
          )
        )
      )
    }
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
  settings(commonSettings: _*).
  settings(assemblySettings: _*).
  settings(
    name := "$name$-auth",
    libraryDependencies ++= authDeps
  )

lazy val appHello = (project in file("./modules/application/hello")).
  dependsOn(infraLambda, infraDynamo, infraS3, infraKinesis).
  settings(commonSettings: _*).
  settings(assemblySettings: _*).
  settings(
    name := "$name$-app-hello",
    libraryDependencies ++= appHelloDeps
  )

lazy val appAccountModified = (project in file("./modules/application/accountmodified")).
  dependsOn(infraLambdaConsumer, infraDomain).
  settings(commonSettings: _*).
  settings(assemblySettings: _*).
  settings(
    name := "$name$-app-account-modified",
    libraryDependencies ++= appAccountModifiedDeps
  )

