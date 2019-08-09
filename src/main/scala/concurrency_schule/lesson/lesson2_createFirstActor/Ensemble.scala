package concurrency_schule.lesson.lesson2_createFirstActor

import akka.actor.ActorSystem
import akka.util.Timeout
import concurrency_schule.lesson.lesson2_createFirstActor.StarWarsSimpleApp.log

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

trait Ensemble {
  log("Creating actor system")
  implicit val actorSystem: ActorSystem = ActorSystem.create("ActorSystem")
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val timeout: Timeout = Timeout(500 millis)

  sys.addShutdownHook {
    log("Shutting down actor system and executionContext")
    actorSystem.terminate()
  }
}
