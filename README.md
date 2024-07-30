# Play Scala gRPC New Relic Security Agent bug demo

This demo project based on the play-grpc sample project demonstrates a bug in the New Relic Security agent.

## To Reproduce

1. Run the application
    ```bash
   sbt run  -J-javaagent:opt/newrelic/newrelic-agent-8.13.0.jar
    ```
2. Make a request to the gRPC endpoint
    ```bash
   grpcurl -insecure -import-path app/protobuf -proto helloworld.proto localhost:9443 helloworld.GreeterService.SayHello
    ```
   
This can be reproduced with any agent version >= 8.9.0. Tested on MacOS 14.6 (Intel), on Java 11.

### Expected Result

A successful gRPC response:

```
{
  "message": "Hello, !"
}
```

### Actual Result

grpcurl reports an error:

```
ERROR:
  Code: Internal
  Message: 

```

and the application logs an exception:

```
2024-07-31 11:15:05 ERROR a.g.s.GrpcExceptionHandler(akka://application) akka.grpc.scaladsl.GrpcExceptionHandler(akka://application) Unhandled error: [Substream Source(substream-out-1) cannot be materialized more than once]
java.lang.IllegalStateException: Substream Source(substream-out-1) cannot be materialized more than once
        at akka.stream.impl.fusing.SubSource$$anon$11.createMaterializedTwiceException(StreamOfStreams.scala:846)
        at akka.stream.impl.fusing.SubSource$$anon$11.<init>(StreamOfStreams.scala:816)
        at akka.stream.impl.fusing.SubSource.createLogic(StreamOfStreams.scala:812)
        at akka.stream.stage.GraphStage.createLogicAndMaterializedValue(GraphStage.scala:105)
        at akka.stream.stage.GraphStageWithMaterializedValue.createLogicAndMaterializedValue(GraphStage.scala:49)
        at akka.stream.impl.GraphStageIsland.materializeAtomic(PhasedFusingActorMaterializer.scala:699)
        at akka.stream.impl.PhasedFusingActorMaterializer.materialize(PhasedFusingActorMaterializer.scala:498)
        at akka.stream.impl.PhasedFusingActorMaterializer.materialize(PhasedFusingActorMaterializer.scala:448)
        at akka.stream.impl.PhasedFusingActorMaterializer.materialize(PhasedFusingActorMaterializer.scala:440)
        at akka.stream.scaladsl.RunnableGraph.run(Flow.scala:764)
        at akka.stream.scaladsl.Source.runWith(Source.scala:118)
        at akka.grpc.scaladsl.GrpcMarshalling$.unmarshal(GrpcMarshalling.scala:55)
        at akka.grpc.scaladsl.GrpcMarshalling$.unmarshal(GrpcMarshalling.scala:61)
        at example.myapp.helloworld.grpc.GreeterServiceHandler$.$anonfun$partial$1(GreeterServiceHandler.scala:117)
        at akka.grpc.scaladsl.GrpcMarshalling$.$anonfun$negotiated$1(GrpcMarshalling.scala:47)
        at scala.Option.map(Option.scala:242)
        at akka.grpc.scaladsl.GrpcMarshalling$.negotiated(GrpcMarshalling.scala:46)
        at example.myapp.helloworld.grpc.GreeterServiceHandler$.handle$1(GreeterServiceHandler.scala:112)
        at example.myapp.helloworld.grpc.GreeterServiceHandler$.$anonfun$partial$5(GreeterServiceHandler.scala:128)
        at scala.PartialFunction$Unlifted.applyOrElse(PartialFunction.scala:347)
        at scala.PartialFunction$OrElse.apply(PartialFunction.scala:266)
        at play.grpc.internal.PlayRouter$$anon$1.apply(PlayRouter.scala:68)
        at play.grpc.internal.PlayRouter$$anon$1.apply(PlayRouter.scala:67)
        at play.core.server.AkkaHttpServer.executeHandler(AkkaHttpServer.scala:438)
        at play.core.server.AkkaHttpServer.handleRequest(AkkaHttpServer.scala:368)
        at play.core.server.AkkaHttpServer.$anonfun$createServerBinding$1(AkkaHttpServer.scala:224)
        at akka.http.scaladsl.AsyncRequestHandler.apply(AsyncRequestHandler.scala:42)
        at akka.http.scaladsl.AsyncRequestHandler.apply(AsyncRequestHandler.scala:19)
        at akka.http.scaladsl.AkkaAsyncRequestHandler.apply(AkkaAsyncRequestHandler.scala:35)
        at akka.http.scaladsl.AkkaAsyncRequestHandler.apply(AkkaAsyncRequestHandler.scala:22)
        at akka.http.impl.engine.http2.Http2Blueprint$.$anonfun$handleWithStreamIdHeader$2(Http2Blueprint.scala:241)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:687)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:467)
        at akka.dispatch.BatchingExecutor$AbstractBatch.processBatch(BatchingExecutor.scala:63)
        at akka.dispatch.BatchingExecutor$BlockableBatch.$anonfun$run$1(BatchingExecutor.scala:100)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.BlockContext$.withBlockContext(BlockContext.scala:94)
        at akka.dispatch.BatchingExecutor$BlockableBatch.run(BatchingExecutor.scala:100)
        at akka.dispatch.TaskInvocation.run(AbstractDispatcher.scala:49)
        at akka.dispatch.ForkJoinExecutorConfigurator$AkkaForkJoinTask.exec(ForkJoinExecutorConfigurator.scala:48)
        at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1020)
        at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1656)
        at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1594)
        at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:183)

```
