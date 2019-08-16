package concurrency_schule.lesson.lesson3_createDifferenteMessages

import akka.actor.{Actor, ActorSystem, Props}
import util.SimpleLog
import akka.pattern.ask
import akka.util.Timeout
import concurrency_schule.lesson.lesson3_createDifferenteMessages.MiddleEarth.{AddHobbit, AddWizard, GetHobbitTeam, HobbitTeam}

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
import scala.language.postfixOps

object LordOfTheRingsApp extends App with SimpleLog {
  implicit val actorSystem: ActorSystem = ActorSystem.create("ActorSystem")
  sys.addShutdownHook {
    actorSystem.terminate()
  }

  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val timeout: Timeout = Timeout(500 millis)

  val middleEarthMailbox = actorSystem.actorOf(MiddleEarth(), "middleEarth")

  middleEarthMailbox ! AddWizard("Gandalf")

  middleEarthMailbox ! AddHobbit("Frodo")

  middleEarthMailbox ! AddHobbit("Sam")

  middleEarthMailbox ! AddHobbit("Merry")

  middleEarthMailbox ! AddHobbit("Pipin")

  val eventualHobbitTeam = middleEarthMailbox ? GetHobbitTeam

  val l = Await.result(eventualHobbitTeam.mapTo[HobbitTeam], Duration.Inf)

  println(s"->>>> The best team ever: $l  ->>>>>>" )

  eventualHobbitTeam.onComplete {
    sys.exit()
  }
}

class MiddleEarth extends Actor {
  var wizards: List[String] = List.empty
  var hobbits: List[String] = List.empty

  override def receive: Receive = {
    case AddWizard(name) ⇒
      println(s"Adding a Wizard $name")
      wizards = name :: wizards

    case AddHobbit(name) ⇒
      println(s"Adding a Hobbit $name")
      hobbits = name :: hobbits

    case GetHobbitTeam ⇒ sender() ! HobbitTeam(hobbits)

  }


}

object MiddleEarth {
  def apply(): Props = Props[MiddleEarth](new MiddleEarth)
  case class AddHobbit(name: String)
  case class AddWizard(name: String)
  case class HobbitTeam(team: List[String])
  case object GetHobbitTeam
}