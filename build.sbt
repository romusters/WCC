name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.1",
  "org.apache.spark" % "spark-core_2.10" % "1.0.2"
)