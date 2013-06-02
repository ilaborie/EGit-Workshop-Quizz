import scala.concurrent.duration.DurationInt
import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Akka
import akka.actor.Props
import actor.WorkerActor

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    reminderDaemon(app)
  }

  def reminderDaemon(app: Application) = {
    Logger.info("Scheduling the worker daemon")
    val workerActor = Akka.system(app).actorOf(Props(new WorkerActor()))
    Akka.system(app).scheduler.schedule(0 seconds, 2 minutes, workerActor, "workerActor")
  }

}
