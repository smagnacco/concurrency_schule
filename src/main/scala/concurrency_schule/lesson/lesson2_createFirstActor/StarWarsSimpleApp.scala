package concurrency_schule.lesson.lesson2_createFirstActor


import concurrency_schule.lesson.lesson2_createFirstActor.GeorgeLucas.{Create, StarWarsCharacter}
import util.SimpleLog
import akka.pattern.ask
import concurrency_schule.lesson.lesson2_createFirstActor.StarWarsSimpleApp.{log}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object StarWarsSimpleApp extends App with SimpleLog with Ensemble with ShowName {

  log("Create a new actor")
  val georgeLucasMailbox = actorSystem.actorOf(GeorgeLucas(), "georgeLucasDirector")

  log("Send first message in order to create hans solo")

  val eventualHanSolo = georgeLucasMailbox ? Create("Hans Solo")

  showName(eventualHanSolo)

  val eventualSupermanFail = georgeLucasMailbox ? Create("Superman")

  showName(eventualSupermanFail)

  sys.exit()


}






trait ShowName {
  def showName(eventual: Future[Any])(implicit executor: ExecutionContext): Unit = {
    eventual.mapTo[StarWarsCharacter].onComplete {
      case Success(value) ⇒
        log(s"FUTURE COMPLETED ->>>> I'm ${value.name}  ->>>>>>" )
      case Failure(exception) ⇒ log(exception.getMessage)
    }
  }
}