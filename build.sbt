import com.lihaoyi.workbench.Plugin._
import com.malliina.sbtutils.SbtProjects

enablePlugins(ScalaJSPlugin)

workbenchSettings

scalaVersion := "2.11.8"

lazy val template = SbtProjects.testableProject("scalajs-playground")

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.0",
  "com.lihaoyi" %%% "scalatags" % "0.6.0",
  "com.typesafe.play" %% "play-json" % "2.5.6"
)

bootSnippet := "MyExample().main(document.getElementById('myDiv'));"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)
