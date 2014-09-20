package controllers

import play.api.libs.json._
import play.api.mvc.{Action, Controller}

object JsonController extends Controller {
  val labels = List("3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00")
  val data_a = List(10, 9, 8, 10, 12, 13.5, 16, 18, 21, 22, 22, 23, 22)
  val data_b = List(6, 5, 6, 8, 9, 11, 12, 14, 15, 15, 13, 14, 13)
  def json(loc: String,data: String) = Action {
    val myJson: JsValue = Json.obj(
      "labels" -> labels,
      "datasets" -> Json.arr(
        Json.obj(
          "label" -> "My First dataset",
          "fillColor" -> "rgba(220,220,220,0.2)",
          "strokeColor" -> "rgba(220,220,220,1)",
          "pointColor" -> "rgba(220,220,220,1)",
          "pointStrokeColor" -> "#fff",
          "pointHighlightFill" -> "#fff",
          "pointHighlightStroke" -> "rgba(220,220,220,1)",
          "data" -> data_a
        ),
        Json.obj(
          "label" -> "My Second dataset",
          "fillColor" -> "rgba(151,187,205,0.2)",
          "strokeColor" -> "rgba(151,187,205,1)",
          "pointColor" -> "rgba(151,187,205,1)",
          "pointStrokeColor" -> "#fff",
          "pointHighlightFill" -> "#fff",
          "pointHighlightStroke" -> "rgba(151,187,205,1)",
          "data" -> data_b
        )
      )
    )

    Ok(Json.prettyPrint(myJson))
  }
}