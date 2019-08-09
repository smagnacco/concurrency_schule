package concurrency_schule.lesson.lesson4_cameo

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import concurrency_schule.lesson.lesson4_cameo.Cameo.{DoYourMagic, Finished}
import concurrency_schule.lesson.lesson4_cameo.First.FirstResponse
import concurrency_schule.lesson.lesson4_cameo.Second.SecondResponse

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

object CameoPatternApp extends App {
  implicit val actorSystem: ActorSystem = ActorSystem.create("ActorSystem")
  sys.addShutdownHook {
    actorSystem.terminate()
  }

  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val timeout: Timeout = Timeout(500 millis)

  val app = new CameoExampleApp()

  val eventualResult = app.getRendezvousResult()

  val result = Await.result(eventualResult, Duration.Inf)

  println(result)

  sys.exit()
}

import akka.pattern.ask
class CameoExampleApp()(implicit actorSystem: ActorSystem) {
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val to: Timeout = Timeout(1 seconds)
  def getRendezvousResult(): Future[Rendezvous] = {
    val cameoActor = actorSystem.actorOf(Cameo())
    (cameoActor ? DoYourMagic).mapTo[Rendezvous]
  }


}

class Cameo extends Actor {
  implicit val ec: ExecutionContext = context.dispatcher
  var first: Option[String] = None
  var second: Option[String] = None
  var continuation: Option[ActorRef] = None
  context.system.scheduler.scheduleOnce(10 milliseconds, self, Finished)

  override def receive: Receive = {
    case DoYourMagic ⇒
      continuation = Some(sender())

      val firstMailbox = context.actorOf(First())
      val secondMailbox = context.actorOf(Second())

      firstMailbox ! DoYourMagic
      secondMailbox ! DoYourMagic


    case FirstResponse(resp) ⇒
      println("First response arrived")
      first = Some(resp)

    case SecondResponse(resp) ⇒
      println("Second response arrived")
      second = Some(resp)

    case Finished ⇒
      if (continuation.isDefined)
        continuation.get ! Rendezvous(first, second)
  }
}

object Cameo {
  def apply(): Props = Props[Cameo](new Cameo())
  case object DoYourMagic
  case object Finished
}

case class Rendezvous(first: Option[String], second: Option[String])

class First extends Actor {
  override def receive: Receive = {
    case DoYourMagic ⇒
      //Thread.sleep(50)
      sender() ! FirstResponse("First")
  }
}
object First {
  def apply(): Props = Props[First](new First())
  case class FirstResponse(response: String)
}

class Second extends Actor {
  override def receive: Receive = {
    case DoYourMagic ⇒ sender() ! SecondResponse("Second")
  }
}
object Second {
  def apply(): Props = Props[Second](new Second())
  case class SecondResponse(response: String)
}