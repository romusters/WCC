package controllers

import play.api._
import play.api.mvc._

class Menuitem(txt: String, location: String){
  var name: String = txt
  var loc: String = location
}

object Application extends Controller {

  def index = Action {
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