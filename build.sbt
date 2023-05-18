name := "concurrency_schule"

version := "0.1"

scalaVersion := "2.13.8"

resolvers ++= Seq(
  Resolver.jcenterRepo,
  Resolver.typesafeRepo("releases"),
  Resolver.bintrayRepo("outworkers", "oss-releases")
)

val akkaVersion = "2.6.19"  // 2.6 is the latest free version
val akkaHttpVersion   = "10.1.8"

libraryDependencies ++= {
  Seq(
    //AKKA
    "com.typesafe.akka"             %% "akka-actor"                        % akkaVersion,
    "com.typesafe.akka"             %% "akka-actor-typed"                  % akkaVersion,
    "com.typesafe.akka"             %% "akka-testkit"                      % akkaVersion,

    //AKKA HTTP
    "com.typesafe.akka"             %% "akka-http"                         % akkaHttpVersion,
    "com.typesafe.akka"             %% "akka-http-testkit"                 % akkaHttpVersion,

    //Test dependencies
    "org.scalatest"                 %% "scalatest"                         % "3.0.8"   % "test",
    "org.mockito"                   %  "mockito-all"                       % "1.10.19" % "test"
  )
}