name := "scala-wordcloud"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.1"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.5.0"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % "0.15.3a",
  "org.http4s" %% "http4s-dsl"          % "0.15.3a",
  "org.http4s" %% "http4s-argonaut"     % "0.15.3a"
)