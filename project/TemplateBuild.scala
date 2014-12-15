import com.lihaoyi.workbench.Plugin._
import com.mle.sbtutils.SbtProjects
import sbt.Keys._
import sbt._

import scala.scalajs.sbtplugin.ScalaJSPlugin._

/**
 * A scala build file template.
 */
object TemplateBuild extends Build {

  lazy val template = SbtProjects.testableProject("scalajs-playground").settings(projectSettings: _*)

  lazy val projectSettings = scalaJSSettings ++ workbenchSettings ++ Seq(
    version := "0.0.1",
    scalaVersion := "2.11.4",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6",
      "com.scalatags" %%% "scalatags" % "0.4.2"
    ),
    bootSnippet := "MyExample().main(document.getElementById('myDiv'));",
    updateBrowsers <<= updateBrowsers.triggeredBy(ScalaJSKeys.fastOptJS in Compile)
  )
}