package actor

/**
  */

import akka.actor.Actor
import play.api.Logger
import model.Repository

class WorkerActor extends Actor {

  private def work(alias: Any) = {
    Logger.debug(s"Push to $alias")

    val process = Runtime.getRuntime.exec(Array("/home/pi/script/pushTo.sh", alias.toString))
    val result = process.waitFor()
    process.destroy()
    result == 0
  }

  def receive = {
    case _ => {
      Logger.debug("Updating a file ...")
      val process = Runtime.getRuntime.exec("/home/pi/script/updateRepo.sh")
      process.waitFor()
      process.destroy()

      // Push to all repository
      Repository.listRepositories foreach work
    }
  }
}
