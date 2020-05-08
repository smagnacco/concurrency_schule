package concurrency_schule.lesson.lesson2_createFirstActor

import akka.actor.{Actor, ActorLogging, Props}
import concurrency_schule.lesson.lesson2_createFirstActor.GeorgeLucas.{Create, StarWarsCharacter, Unemployed}

class GeorgeLucas extends Actor with ActorLogging {
  override def receive: Receive = {
    case Create(character) if character == "Hans Solo" ⇒
      val continuation = sender()
      sender() ! StarWarsCharacter("Hans Solo")

    case Create(character) ⇒
      log.warning(s"A can't play as $character")
      sender() ! Unemployed(character)

    case _ ⇒
      log.error("Unexpected message")
      sender() ! Left(new IllegalArgumentException("Unexpected message"))

  }
}

object GeorgeLucas {
  def apply():Props = Props[GeorgeLucas](new GeorgeLucas())

  case class Create(character: String)

  case class StarWarsCharacter(name: String) extends Person

  case class Unemployed(name: String) extends Person

}

trait Person {
  def name: String
}
