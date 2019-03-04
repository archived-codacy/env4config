resolvers := Seq(DefaultMavenRepository, Resolver.jcenterRepo, Resolver.sonatypeRepo("releases"))

addSbtPlugin("com.codacy" % "codacy-sbt-plugin" % "14.0.0")

addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.1.0")
