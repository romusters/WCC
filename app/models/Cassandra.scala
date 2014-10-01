package models

import java.util.Date

import com.datastax.driver.core.{Cluster, Metadata}
import akka.actor.{ActorSystem, Actor, Props}
import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer


/**
 * Created by laptop on 28-9-14.
 */

class MyActor(cluster: Cluster) extends Actor {
  val session = cluster.connect()

  val loc = "north"
  val temperature = 20.0
  val li = 2

  def receive = {
    case "addData" =>
      session.execute("CREATE TABLE IF NOT EXISTS wcc.sensordata (loc text, time timestamp, temperature float, li float, primary key(loc, time));")
      session.execute("INSERT INTO wcc.sensordata(loc, time, temperature, li) VALUES ('" + loc + "', dateof(now())," + temperature + "," + li + ");")
    case _ =>
      println("unknown thing received")
  }

}

object CassandraManager {
  val cluster = Cluster.builder.addContactPoint("127.0.0.1").build
  val metadata: Metadata = cluster.getMetadata
  System.out.printf("Connected to cluster: %s\n", metadata.getClusterName)

  import scala.collection.JavaConversions._

  for (host <- metadata.getAllHosts) {
    System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter, host.getAddress, host.getRack)

  }
  val session = cluster.connect()

  val system = ActorSystem("TestSystem")
  val myActor = system.actorOf(Props(new MyActor(cluster)), name = "myactor")

  import system.dispatcher

  val cancellable = system.scheduler.schedule(0 milliseconds, 1000 milliseconds, myActor, "addData")

  def get(loc: String, d_type: String): (List[Float], List[Date]) = {
    val results = session.execute("SELECT * FROM wcc.sensordata WHERE loc = 'north' ORDER BY time DESC LIMIT 10;")
    val result = new ListBuffer[Float]
    val label = new ListBuffer[Date]
    for (row <- results) {
      result += row.getFloat(d_type)
      label += row.getDate("time")
    }
    return (result.toList, label.toList)
  }

  def get_predicted(loc: String, d_type: String): List[Float] = {
    return List(6, 7, 8)
  }

  def close {
    cluster.close
  }
}