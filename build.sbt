name := "concurrency_schule"

version := "0.1"

scalaVersion := "2.13.0"

resolvers ++= Seq(
  "Nexus despegar" at "http://nexus.despegar.it:8080/nexus/content/groups/public/",
  "Nexus despegar miami" at "http://nexus:8080/nexus/content/groups/public/",
  Resolver.jcenterRepo,
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.typesafeRepo("releases"),
  Resolver.bintrayRepo("kamon-io", "releases"),
  Resolver.bintrayRepo("kamon-io", "snapshots"),
  Resolver.bintrayRepo("outworkers", "oss-releases")
)

val akkaVersion       = "2.5.23"
val akkaHttpVersion   = "10.1.8"

libraryDependencies ++= {
  Seq(
    //AKKA
    "com.typesafe.akka"             %% "akka-actor"                        % akkaVersion,
    "com.typesafe.akka"             %% "akka-testkit"                      % akkaVersion,

    //AKKA HTTP
    "com.typesafe.akka"             %% "akka-http"                         % akkaHttpVersion,
    "com.typesafe.akka"             %% "akka-http-testkit"                 % akkaHttpVersion,

    //Test dependencies
    "org.scalatest"                 %% "scalatest"                         % "3.0.8"   % "test",
    "org.mockito"                   %  "mockito-all"                       % "1.10.19" % "test"
  )
}