package models

import com.datastax.driver.core.{ResultSet, Cluster, Host, Metadata}

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

  def get(loc: String, d_type: String): List[Float] = {
    val results = session.execute("SELECT * FROM wcc.sensordata WHERE loc = '" + loc + "';")
    val result = List()
    for (row <- results) {
      result :+ row.getFloat(d_type)
      println(row.getFloat(d_type))
    }
    println(result.mkString("[",",","]"))
    return result
  }

  def get_predicted(loc: String, d_type: String): List[Float] = {
    return List(6)
  }

  def close {
    cluster.close
  }
}