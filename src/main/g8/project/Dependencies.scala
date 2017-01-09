import sbt._

object Dependencies {

  // Specs2
  val specs2Core = "org.specs2" %% "specs2-core" % "3.8.6"
  val specs2Mock = "org.specs2" %% "specs2-mock" % "3.8.6"
  val specs2JUnit = "org.specs2" %% "specs2-junit" % "3.8.6"

  // Amazon
  val awsSDKVersion = "1.11.52"
  val awsSDKDynamoDB = "com.amazonaws" % "aws-java-sdk-dynamodb" % awsSDKVersion
  val awsSDKS3       = "com.amazonaws" % "aws-java-sdk-s3" % awsSDKVersion
  val awsSDKKinesis  = "com.amazonaws" % "aws-java-sdk-kinesis" % awsSDKVersion

  // Amazon Lambda
  val lambdaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"

  // spray-json
  val sprayJson = "io.spray" %%  "spray-json" % "1.3.2"

  // config
  val config = "com.typesafe" % "config" % "1.3.1"

  val rootDeps = Seq(
    specs2Core % Test,
    specs2Mock % Test,
    specs2JUnit % Test
  )

  val domainDeps = Seq(
  )

  val infraLambdaDeps = Seq(
    lambdaCore,
    sprayJson
  )

  val infraLambdaConsumerDeps = Seq(
    sprayJson
  )

  val infraDomainDeps = Seq(
    sprayJson
  )

  val infraDynamoDeps = Seq(
    awsSDKDynamoDB
  )

  val infraS3Deps = Seq(
    awsSDKS3
  )

  val infraKinesisDeps = Seq(
    awsSDKKinesis
  )

  val infraElasticSearchDeps = Seq(
  )

  val authDeps = Seq(
    lambdaCore,
    sprayJson
  )

  val appHelloDeps = Seq(
  )

  val appAccountModifiedDeps = Seq(
  )

}
