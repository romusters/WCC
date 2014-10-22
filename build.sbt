name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.2",
  "com.datastax.spark" %% "spark-cassandra-connector" % "1.1.0-alpha4" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-core" % "1.1.0",
  "org.apache.spark" %% "spark-sql" % "1.1.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.6"
)