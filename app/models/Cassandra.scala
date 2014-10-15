package models

import com.datastax.driver.core.{ResultSet, BoundStatement, Cluster, Metadata}
import akka.actor.{ActorSystem, Actor, Props}
import scala.concurrent.duration._
import java.util.{Calendar, Date, Random}
import java.text._


import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

/**
 * Created by laptop on 28-9-14.
 */

object SimpleUtility{

  def SimpleApp {
    val conf = new SparkConf()
        .setMaster("local[4]")
        .setAppName("play-scala")
    val sc = new SparkContext(conf)

    val count = sc.parallelize(1 to 10000).map{i =>
      val x = Math.random()
      val y = Math.random()
      if (x*x + y*y < 1) 1 else 0
    }.reduce(_ + _)
    println("Pi is roughly " + 4.0 * count / 10000)
  }
}

class Data(location: String, temperature: Float, light: Float) {

  def randNum(max: Int): Float = {
    val rnd = new scala.util.Random
    val range = -max to max

    /*
    val today = Calendar.getInstance().getTime()
    val minuteFormat = new SimpleDateFormat("mm")
    val currentMinuteAsString = minuteFormat.format(today)
    val currentMinute = Integer.parseInt(currentMinuteAsString)

    return (temperature * Math.sin(currentMinute)).toFloat
    */

    return range(rnd.nextInt(range length))
  }

  val loc: String = location
  val temp: Float = temperature + randNum(3)
  val li: Float = light + randNum(1)
}

class DataActor(cluster: Cluster) extends Actor {
  val session = cluster.connect()
  //val prepare_createTable = new BoundStatement(session.prepare("CREATE TABLE IF NOT EXISTS wcc.sensordata (loc text, time timestamp, temperature float, li float, primary key(loc, time));"))
  val prepare_addData = new BoundStatement(session.prepare("INSERT INTO wcc.sensordata(loc, time, temperature, li) VALUES (?, dateOf(now()), ?, ?);"))
  //val prepare_getData = new BoundStatement(session.prepare("SELECT * FROM wcc.sensordata WHERE loc = ? ORDER BY time DESC LIMIT 1;"))

  def addData(data: Data) {
    prepare_addData.setString(0, data.loc)
    prepare_addData.setFloat(1, data.temp)
    prepare_addData.setFloat(2, data.li)
    //session.execute(prepare_createTable)
    session.executeAsync(prepare_addData)
  }

  def receive: Receive = {
    case "addData" =>
      addData(new Data("north", 18, 2))
      addData(new Data("east", 19, 2))
      addData(new Data("south", 21, 2))
      addData(new Data("west", 20, 2))
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

  /* create data object and add it to the database */
  val cancellable = system.scheduler.schedule(0 milliseconds, 1000 milliseconds, dataActor, "addData")

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
/*
    val logFile = "logs/spark.log" // Should be some file on your system
    val conf = new SparkConf(false) // skip loading external settings
        .setMaster("local[4]") // run locally with enough threads
        .setAppName("firstSparkApp")
        .set("spark.logConf", "true")
        .set("spark.driver.host", "localhost")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numSparks = logData.filter(line => line.contains("Spark")).count()
    println("Lines with Spark: %s".format(numSparks))
*/


    return 6
  }

  def close {
    cluster.close
  }
}