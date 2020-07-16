name := """todo-api"""
organization := "com.todo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(guice, ws,
  "com.pauldijou" %% "jwt-play" % "4.2.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.20.11-play27",
  "org.reactivemongo" %% "reactivemongo-play-json-compat" % "0.20.12-play28",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "org.mockito" % "mockito-all" % "1.10.19" % Test,
//  "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.38.0" % Test,
//  "com.dimafeng" %% "testcontainers-scala-mongodb" % "0.38.0" % "test",
//  "org.testcontainers" % "mongodb" % "1.14.3" % Test
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.todo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.todo.binders._"
