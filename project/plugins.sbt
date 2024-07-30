resolvers += "Akka library repository".at("https://repo.akka.io/maven")
libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
enablePlugins(BuildInfoPlugin)
val playGrpcV = "0.11.1"
buildInfoKeys := Seq[BuildInfoKey]("playGrpcVersion" -> playGrpcV)
buildInfoPackage := "play.scala.grpc.sample"


addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.9.5")

// #grpc_sbt_plugin
// project/plugins.sbt
addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "2.1.6")
libraryDependencies += "com.lightbend.play" %% "play-grpc-generators" % "0.9.1"
// #grpc_sbt_plugin
