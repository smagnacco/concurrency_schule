package concurrency_schule.lesson.pingpong

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

sealed trait PingPongMessage
case class Hola(replyTo: ActorRef[Chao]) extends PingPongMessage
case class Chao(replyTo: ActorRef[Hola]) extends PingPongMessage

object HolaActor {
  def apply(): Behavior[Hola] = {
    Behaviors.receive { (context, message) =>
      println("Hola")
      message.replyTo ! Chao(context.self)
      Behaviors.same
    }
  }
}

object ChaoActor {
  def apply(): Behavior[Chao] = {
    Behaviors.receive { (context, message) =>
      println("Chao")
      message.replyTo ! Hola(context.self)
      Behaviors.same
    }
  }
}

object HolaChaoExample extends App {

  val behavior = Behaviors.setup[Nothing] {
    context: ActorContext[Nothing] =>

      val holaActor: ActorRef[Hola] = context.spawn(HolaActor(), "holaActor")

      val chaoActor: ActorRef[Chao] = context.spawn(ChaoActor(), "chaoActor")

      holaActor ! Hola(chaoActor)

    Behaviors.empty[Nothing]
  }

  val system = ActorSystem[Nothing](behavior, "HolaChaoSystem")

}
