Nice.scalaProject

name          := "scala-show-off"
organization  := "ohnosequences"

bucketSuffix  := "era7.com"

scalaVersion        := "2.11.6"
crossScalaVersions  := Seq("2.10.5")

libraryDependencies ++= Seq(
  "com.thinkaurelius.titan" %  "titan-core"       % "0.5.4",
  "org.scalatest"           %% "scalatest"        % "2.2.4" % Test,
  "org.slf4j"               %  "slf4j-nop"        % "1.7.5" % Test
  // ^ getting rid of the annoying warning about logging ^
)

// shows time for each test:
testOptions in Test += Tests.Argument("-oD")

//scalacOptions ++= Seq("-optimise", "-Yinline", "-Yinline-warnings")
