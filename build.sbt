
import play.grpc.gen.scaladsl.{ PlayScalaClientCodeGenerator, PlayScalaServerCodeGenerator }
import com.typesafe.sbt.packager.docker.{ Cmd, CmdLike, DockerAlias, ExecCmd }
import play.scala.grpc.sample.BuildInfo

name := "play-scala-grpc-nr-bug"
version := "1.0-SNAPSHOT"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")


val akkaVersion = "2.6.20"
val akkaHttpVersion = "10.2.10"

// #grpc_play_plugins
// build.sbt
lazy val `play-scala-grpc-nr-bug` = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(AkkaGrpcPlugin) // enables source generation for gRPC
  .enablePlugins(PlayAkkaHttp2Support) // enables serving HTTP/2 and gRPC
// #grpc_play_plugins
    .settings(
      libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always,
      akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala),
      // #grpc_client_generators
      // build.sbt
      akkaGrpcExtraGenerators += PlayScalaClientCodeGenerator,
      // #grpc_client_generators
      // #grpc_server_generators
      // build.sbt
      akkaGrpcExtraGenerators += PlayScalaServerCodeGenerator,
      Test / javaOptions += "-Dtestserver.httpsport=0",
      // #grpc_server_generators
      PlayKeys.devSettings ++= Seq(
        "play.server.http.port" -> "disabled",
        "play.server.https.port" -> "9443",
        // Configures the keystore to use in Dev mode. This setting is equivalent to `play.server.https.keyStore.path`
        // in `application.conf`.
        "play.server.https.keyStore.path" -> "conf/selfsigned.keystore",
      )
    )
    .settings(
      libraryDependencies ++= CompileDeps
    )

val CompileDeps = Seq(
  guice,
  "com.typesafe.play"      %% "play-grpc-runtime"     % BuildInfo.playGrpcVersion,
  "com.typesafe.akka"       %% "akka-discovery"       % akkaVersion,
  "com.typesafe.akka"       %% "akka-actor-typed"       % akkaVersion,
  "com.typesafe.akka"       %% "akka-serialization-jackson"       % akkaVersion,
  "com.typesafe.akka"       %% "akka-slf4j"                 % akkaVersion,
  "com.typesafe.akka"       %% "akka-actor-typed"       % akkaVersion,
  "com.typesafe.akka"       %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka"       %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka"       %% "akka-http2-support"   % akkaHttpVersion,
  // Test Database
  "com.h2database" % "h2" % "2.2.224"
)

val playVersion = play.core.PlayVersion.current

scalaVersion := "2.13.14"
scalacOptions ++= List("-encoding", "utf8", "-deprecation", "-feature", "-unchecked")
// Needed for ssl-config to create self signed certificated under Java 17
Test / javaOptions ++= List("--add-exports=java.base/sun.security.x509=ALL-UNNAMED")
