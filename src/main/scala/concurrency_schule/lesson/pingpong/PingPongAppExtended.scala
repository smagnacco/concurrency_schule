package concurrency_schule.lesson.pingpong

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}

case class PingTo(ref: ActorRef, i: Int)
case class PongTo(ref: ActorRef, i: Int)

class PingToActor extends Actor {
  def receive: Receive = {

    case PingTo(pongActor: ActorRef, i) =>
      println("Ping__________ " + i )
      pongActor ! PongTo(self, i + 1)
  }
}

class PongToActor extends Actor {
  def receive: Receive = {
    case PongTo(pingTo, i) =>
      println("__________Pong " + i)
      pingTo ! PingTo(self, i + 1)
  }
}

object PingPongExtendedExample extends App {
  val system = ActorSystem("PingPongSystem")
  private val pingActors : List[ActorRef] = Range(0,1000).toList.map {i => system.actorOf(Props[PingToActor], "pingActor" + i)}
  private val pongActors: List[ActorRef] = Range(0,1000).toList.map{ i => system.actorOf(Props[PongToActor], "pongActor" + i)}

  // Start the ping-pong interaction
  //pingActor ! Ping
  for {
    pingActor <- pingActors
    pongActor <- pongActors
  } {
    pingActor ! PingTo(pongActor, 0)
    pongActor ! PongTo(pingActor, 0)
  }

  Thread.sleep(10000)
  for {
    pingActor1 <- pingActors
    pongActor1 <- pongActors
  } {
    pingActor1 ! PoisonPill
    pongActor1 ! PoisonPill
  }

  system.terminate()
}

