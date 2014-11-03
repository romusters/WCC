package models

import com.datastax.driver.core.{Cluster, Metadata}
import java.util.{Date}
import java.text._

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

  def get(loc: String, d_type: String, n: Int): (List[Float], List[String]) = {
    val results = session.execute("SELECT * FROM wcc.sensordata WHERE loc = '" + loc + "' ORDER BY time DESC LIMIT " + n + ";")
    val q_type = if (d_type == "light intensity") "li" else d_type;
    val format = new SimpleDateFormat("HH:mm:ss")

    val data = new ListBuffer[Float]
    val labels = new ListBuffer[String]

    for (row <- results) {
      data += row.getFloat(q_type)
      labels += format.format(row.getDate("time"))
    }
    return (data.toList, labels.toList)
  }

  def get_predicted(loc: String, d_type: String, n: Int): List[Float] = {
    val results = session.execute("SELECT * FROM wcc.avg WHERE loc = '" + loc + "' ORDER BY time DESC LIMIT " + n + ";")
    val q_type = if (d_type == "light intensity") "li" else d_type;

    val data = new ListBuffer[Float]

    for (row <- results) {
      data += row.getFloat(q_type)
    }

    return data.toList
  }

  def close {
    cluster.close
  }
}