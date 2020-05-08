package concurrency_schule.lesson.lesson3_createDifferenteMessages

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import concurrency_schule.lesson.lesson3_createDifferenteMessages.MiddleEarth._
import util.SimpleLog

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
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
  val eventualWizzards: Future[Wizzards] = (middleEarthMailbox ? GetWizzards).mapTo[Wizzards]

  eventualWizzards.foreach(w => println(w.team) )

  Future.sequence(List(eventualHobbitTeam, eventualWizzards)).onComplete {
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
    case GetWizzards => sender() ! Wizzards(wizards)

  }


}

object MiddleEarth {
  def apply(): Props = Props[MiddleEarth](new MiddleEarth)
  case class AddHobbit(name: String)
  case class AddWizard(name: String)
  case class HobbitTeam(team: List[String])
  case class Wizzards(team: List[String])
  case object GetHobbitTeam
  case object GetWizzards
}