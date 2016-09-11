import sbt._

object BuildBuild extends Build {

  override lazy val settings = super.settings ++ sbtPlugins

  def sbtPlugins = Seq(
    "com.malliina" %% "sbt-utils" % "0.4.0",
    "org.scala-js" % "sbt-scalajs" % "0.6.12",
    "com.lihaoyi" % "workbench" % "0.2.3"
  ) map addSbtPlugin

  override lazy val projects = Seq(root)
  lazy val root = Project("plugins", file("."))
}
