package models

import com.datastax.driver.core.{ResultSet, BoundStatement, Cluster, Metadata}
import akka.actor.{ActorSystem, Actor, Props}
import scala.concurrent.duration._
import java.util.{Calendar, Date, Random}
import java.text._

import org.apache.spark.{SparkContext, SparkConf}
import com.datastax.spark.connector._

/**
 * Created by laptop on 28-9-14.
 */


object SparkHelper {
  def getConnection(): SparkContext = {
    val conf = new SparkConf(true)
      .set("spark.cassandra.connection.host", "127.0.0.1")
    val sc = new SparkContext("local[4]", "test", conf)
    sc
  }
}

class ComputeSome {
  def compute() {
    val connection = SparkHelper getConnection()
    val rdd = connection.cassandraTable("wcc", "sensordata").select("loc", "temperature")
    val flatMap = rdd.map(s => (s.getString("loc"), s.getFloat("temperature")))
    val grouped = flatMap.groupBy(kv => kv._1)
    val gpf = grouped.map { case (word, list) => word -> list.map(kv => kv._2)}.cache()
    val rf = gpf.map { case (word, list) => (word, list reduce (_ + _), list.size)}
    val avg = rf.map { case (a, b, c) => (a, b / c)}
    avg.foreach(f => System.out.println(f))
  }
}

class SensorData(location: String, temperature: Float, light: Float) {

  def randNum(max: Int): Float = {
    val rnd = new scala.util.Random
    val range = -max to max
    return range(rnd.nextInt(range length))
  }

  val loc: String = location
  val temp: Float = temperature + randNum(3)
  val li: Float = light + randNum(1)
}

class DataActor(cluster: Cluster) extends Actor {
  val session = cluster.connect()
  //val prepare_createTable = new BoundStatement(session.prepare("CREATE TABLE IF NOT EXISTS wcc.sensordata (loc text, time timestamp, temperature float, li float, primary key(loc, time));"))
  val prepare_addSensorData = new BoundStatement(session.prepare("INSERT INTO wcc.sensordata(loc, time, temperature, li) VALUES (?, dateOf(now()), ?, ?);"))
  val prepare_addWeatherData = new BoundStatement(session.prepare("INSERT INTO wcc.weatherdata(source, time, temperature) VALUES(?, ?, ?);"))
  //val prepare_getData = new BoundStatement(session.prepare("SELECT * FROM wcc.sensordata WHERE loc = ? ORDER BY time DESC LIMIT 1;"))

  def addSensorData(data: SensorData) {
    prepare_addSensorData.setString(0, data.loc)
    prepare_addSensorData.setFloat(1, data.temp)
    prepare_addSensorData.setFloat(2, data.li)
    session.executeAsync(prepare_addSensorData)
  }

  def addWeatherData(source: String, time: Date, temp: Float): Unit = {
    prepare_addWeatherData.setString(0, source)
    prepare_addWeatherData.setDate(1, time)
    prepare_addWeatherData.setFloat(2, temp)
    session.executeAsync(prepare_addWeatherData)
  }

  def receive: Receive = {
    case "addSensorData" =>
      addSensorData(new SensorData("north", 18, 2))
      addSensorData(new SensorData("east", 19, 2))
      addSensorData(new SensorData("south", 21, 2))
      addSensorData(new SensorData("west", 20, 2))
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

  val system = ActorSystem("DataSystem")
  val dataActor = system.actorOf(Props(new DataActor(cluster)), name = "dataactor")

  import system.dispatcher

  /* schedule adding sensor data to database */
  val cancellable = system.scheduler.schedule(0 milliseconds, 1000 milliseconds, dataActor, "addSensorData")

  def get(loc: String, d_type: String): (Float, String) = {
    val results = session.execute("SELECT * FROM wcc.sensordata WHERE loc = '" + loc + "' ORDER BY time DESC LIMIT 1;")
    var result: Float = 0
    var date: Date = new Date()
    var q_type = ""
    val format = new SimpleDateFormat("HH:mm:ss")

    if(d_type == "light intensity"){
      q_type = "li"
    } else {
      q_type = d_type
    }

    for (row <- results) {
      result = row.getFloat(q_type)
      date = row.getDate("time")
    }
    return (result, format.format(date))
  }

  def get_predicted(loc: String, d_type: String): Float = {
    return 6
  }

  def close {
    cluster.close
  }
}