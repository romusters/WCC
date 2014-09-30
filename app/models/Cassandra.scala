package models

import java.util.Date

import com.datastax.driver.core.{ResultSet, Cluster, Host, Metadata}
import org.joda.time.DateTime
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
 * Created by laptop on 28-9-14.
 */

object CassandraManager {
  val cluster = Cluster.builder.addContactPoint("127.0.0.1").build
  val metadata: Metadata = cluster.getMetadata
  System.out.printf("Connected to cluster: %s\n", metadata.getClusterName)

  import scala.collection.JavaConversions._

  for (host <- metadata.getAllHosts) {
    System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter, host.getAddress, host.getRack)

  }
  val session = cluster.connect()

  
  def add() = {
    val loc = "east"
    var time = DateTime.now
    val temperature = 20.0
    val li = 2
    var x = 0
    for(x <- 1 to 100){
      time = time.plusSeconds(10)
      session.execute("insert into wcc.sensordata(loc, time,temperature,li) values('east', '" + time + "',"  + temperature + "," + li + ") using ttl 10;");
    }

  }
  def get(loc: String, d_type: String): (List[Float], List[Date]) = {
    add()
    val results = session.execute("SELECT * FROM wcc.sensordata WHERE loc = 'west' ORDER BY time ASC LIMIT 100;")
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