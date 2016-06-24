val derby = "org.apache.derby" % "derby" % "10.4.1.3"

lazy val commonSettings = Seq(
  organization := "org.planteome",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "samara",
    libraryDependencies ++= Seq(
    "net.ruippeixotog" %% "scala-scraper" % "1.0.0",
    "org.scalatest" %% "scalatest" % "2.2.5" % "test"
  ))
