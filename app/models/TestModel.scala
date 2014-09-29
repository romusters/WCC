package models

import com.datastax.driver.core.{ResultSet, Cluster, Host, Metadata}
import org.joda.time.DateTime

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
  def executeQuery(cql: String): Float = {
    val results = session.execute(cql)
    var result: Float =0
    for (row <- results) {
      result = row.getFloat("temp")
    }
    return result
  }

  def close {
    cluster.close
  }
}

class TestModel{
  def add(): Unit ={

  }
  def get(location: String): Float ={
    // select * from temperatures where time == someTime
    val client = CassandraManager

    var result:Float = client.executeQuery("SELECT * FROM simplex.sensordata WHERE location = 'North';")
    println(result)
    client.close
    return result
  }
}
