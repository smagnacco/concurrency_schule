package concurrency_schule.lesson.lesson2_createFirstActor


import akka.actor.ActorRef
import concurrency_schule.lesson.lesson2_createFirstActor.GeorgeLucas.Create
import util.SimpleLog
import akka.pattern.ask
import concurrency_schule.lesson.lesson2_createFirstActor.StarWarsSimpleApp.log

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object StarWarsSimpleApp extends App with SimpleLog with Ensemble with ShowName {

  log("Create a new actor")
  val georgeLucasMailbox: ActorRef = actorSystem.actorOf(GeorgeLucas(), "georgeLucasDirector")

  log("Send first message in order to create hans solo")

  val eventualHanSolo: Future[Any] = georgeLucasMailbox ? Create("Hans Solo")

  showName(eventualHanSolo)

  val eventualSupermanFail: Future[Any] = georgeLucasMailbox ? Create("Superman")

  showName(eventualSupermanFail)

  Future.sequence(List(eventualHanSolo, eventualSupermanFail)).onComplete {
    sys.exit()
  }

}






trait ShowName {
  def showName(eventual: Future[Any])(implicit executor: ExecutionContext): Unit = {
    eventual.mapTo[Person].onComplete {
      case Success(value) ⇒
        log(s"FUTURE COMPLETED ->>>> I'm ${value.name}  ->>>>>>" )
      case Failure(exception) ⇒ log(exception.getMessage)
    }
  }
}