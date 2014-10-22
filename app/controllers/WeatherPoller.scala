package controllers
import scala.xml.XML
import play.api.mvc._
/**
 * Created by laptop on 8-10-14.
 */
object WeatherPoller extends Controller {
  def getTemperature(): Unit ={
    val xml = scala.xml.XML.load("http://weather.yahooapis.com/forecastrss?w=729549&u=c");
    println(xml \ "channel" \ "item" \ "forecast")
  }
}
