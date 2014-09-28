package controllers

import play.api._
import play.api.mvc._

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Host
import com.datastax.driver.core.Metadata
/**
 * Created by laptop on 28-9-14.
 */

class SimpleClient {
  def connect(node: String) {
    cluster = Cluster.builder.addContactPoint(node).build
    val metadata: Metadata = cluster.getMetadata
    System.out.printf("Connected to cluster: %s\n", metadata.getClusterName)
    import scala.collection.JavaConversions._
    for (host <- metadata.getAllHosts) {
      System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter, host.getAddress, host.getRack)
    }
  }

  def close {
    cluster.close
  }

  private var cluster: Cluster = null
}



class Menuitem(txt: String, location: String){
  var name: String = txt
  var loc: String = location
}

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(Menuitem.list))
  }
  val client = new SimpleClient();
  client.connect("127.0.0.1");
  client.close;

}

object Menuitem extends Controller {
  val list = List(
    new Menuitem("North","#"),
    new Menuitem("East","#"),
    new Menuitem("South","#"),
    new Menuitem("West","#")
  )
}