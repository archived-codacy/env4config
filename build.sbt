name := "env4config"

scalaVersion := "2.12.8"

crossPaths := false

autoScalaLibrary := false

description := "Environment variables resolution for Typesafe Config"

scmInfo := Some(ScmInfo(url("https://github.com/codacy/env4config"), "scm:git:git@github.com:codacy/env4config.git"))

// this setting is not picked up properly from the plugin
pgpPassphrase := Option(System.getenv("SONATYPE_GPG_PASSPHRASE")).map(_.toCharArray)

resolvers ~= { _.filterNot(_.name.toLowerCase.contains("codacy")) }

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.3" % "provided",
  "org.scala-lang" % "scala-library" % scalaVersion.value % Test,
  "junit" % "junit" % "4.11" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test
)

scalacOptions in Test += "-Xlint:_,-missing-interpolator"

javacOptions += "-Xdoclint:none"

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")

Test / envVars ++= Map("CONFIG_testList_0" -> "0", "CONFIG_testList_1" -> "1")

fork in Test := true
cancelable in Global := true

publicMvnPublish
