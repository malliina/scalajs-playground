import sbt._

object BuildBuild extends Build {

  override lazy val settings = super.settings ++ sbtPlugins

  def sbtPlugins = Seq(
    "com.timushev.sbt" % "sbt-updates" % "0.1.6",
    "com.github.malliina" %% "sbt-utils" % "0.0.5",
    "org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.5.5",
    "com.lihaoyi" % "workbench" % "0.2.3"
  ) map addSbtPlugin

  override lazy val projects = Seq(root)
  lazy val root = Project("plugins", file("."))
}