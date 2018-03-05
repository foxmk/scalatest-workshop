lazy val root = project
  .in(file("."))
  .settings(
    name := "scalatest-workshop",
    version := "0.1",
    scalaVersion := "2.12.4",
    scalacOptions += "-Ypartial-unification",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "1.0.1",
      "org.scalatest" %% "scalatest" % "3.0.4" % Test
    )
  )
