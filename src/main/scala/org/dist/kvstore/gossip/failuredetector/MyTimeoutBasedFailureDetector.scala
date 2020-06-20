package org.dist.kvstore.gossip.failuredetector

import java.util
import java.util.concurrent

class MyTimeoutBasedFailureDetector[T] extends FailureDetector[T]  {
  val serverHeartBeatReceived = new util.HashMap[T, Long]
  val timeToExpiry = concurrent.TimeUnit.MILLISECONDS.toNanos(100)
  override def heartBeatCheck(): Unit =
    {
      val currentTime: Long = System.nanoTime()
      serverHeartBeatReceived.forEach((serverId, lastHeartbeatTime) => {
        if (currentTime - lastHeartbeatTime > timeToExpiry)
          markDown(serverId)
      })

    }

  override def heartBeatReceived(serverId: T): Unit = {
    serverHeartBeatReceived.put(serverId, System.nanoTime())
    markUp(serverId)
  }
}
