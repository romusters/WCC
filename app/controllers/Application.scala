package controllers

import play.api.mvc._
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

class Menuitem(txt: String, location: String){
  var name: String = txt
  var loc: String = location
}

object Application extends Controller {

  def index = Action {
    WeatherPoller.getTemperature()
    val conf = new SparkConf(true).set("spark.cassandra.connection.host", "127.0.0.1")
    Ok(views.html.index(Menuitem.list))
  }

}

object Menuitem extends Controller {
  val list = List(
    new Menuitem("North","#"),
    new Menuitem("East","#"),
    new Menuitem("South","#"),
    new Menuitem("West","#")
  )
}