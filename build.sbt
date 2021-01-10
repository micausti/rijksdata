val Http4sVersion = "0.21.3"
val CirceVersion = "0.13.0"
val Specs2Version = "4.9.3"
val LogbackVersion = "1.2.3"
val ScalaLoggingVersion = "3.9.0"
val log4CatsVersion = "1.1.1"

lazy val root = (project in file("."))
  .settings(
    organization := "com.michelle",
    name := "rijksdata",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      "org.http4s"                  %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"                  %% "http4s-blaze-client" % Http4sVersion,
        "org.http4s"                %% "http4s-circe"        % Http4sVersion,
      "org.http4s"                  %% "http4s-dsl"          % Http4sVersion,
      "io.circe"                    %% "circe-generic"       % CirceVersion,
      "io.circe"                    %% "circe-parser"       % CirceVersion,
      "org.specs2"                  %% "specs2-core"         % Specs2Version % "test",
      "ch.qos.logback"              %  "logback-classic"     % LogbackVersion,
      "io.circe"                    %% "circe-optics"        % CirceVersion,
      "com.amazonaws"              % "aws-java-sdk-sqs"       % "1.11.855",
      "com.amazonaws"              % "aws-lambda-java-events" % "3.2.0",
      "io.chrisdavenport" %% "log4cats-slf4j" % log4CatsVersion,
      "org.scalatest"  %% "scalatest"                        % "3.1.1" % "test",
      "org.scalamock"  %% "scalamock"                        % "4.4.0" % "test",
      "org.scalacheck" %% "scalacheck"                       % "1.14.0" % "test"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
)
