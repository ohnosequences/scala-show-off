resolvers ++= Seq(
  "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com"
)

addSbtPlugin("ohnosequences" % "nice-sbt-settings" % "0.5.1")
