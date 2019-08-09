package concurrency_schule.lesson.lesson1_createActorSystem

import akka.actor.ActorSystem
import util.SimpleLog

object StartStopActorSystem extends App with SimpleLog {

  log("Starting up StarWars Actor System")

  val actorSystem: ActorSystem = ActorSystem.create("StarWars")


  log("Shutting down StarWars actor system")

  actorSystem.terminate()

}




