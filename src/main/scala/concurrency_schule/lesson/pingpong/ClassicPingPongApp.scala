package concurrency_schule.lesson.pingpong

import akka.actor.{Actor, ActorSystem, Props}

case object Ping
case object Pong

class PingActor extends Actor {
  def receive: Receive = {
    case Ping =>
      println("Ping")
      sender() ! Pong
  }
}

class PongActor extends Actor {
  def receive: Receive = {
    case Pong =>
      println("Pong")
      sender() ! Ping
  }
}

object PingPongExample extends App {
  val system = ActorSystem("PingPongSystem")
  val pingActor = system.actorOf(Props[PingActor], "pingActor")
  val pongActor = system.actorOf(Props[PongActor], "pongActor")

  // Start the ping-pong interaction
  pingActor ! Ping
}