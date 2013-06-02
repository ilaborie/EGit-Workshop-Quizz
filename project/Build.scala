import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Quizz"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.eclipse.jgit" % "org.eclipse.jgit" % "2.3.1.201302201838-r",
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
