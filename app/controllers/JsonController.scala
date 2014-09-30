package controllers

import play.api.libs.json._
import play.api.libs.EventSource
import play.api.mvc.{Action, Controller}
import play.api.libs.iteratee.{Concurrent, Enumerator}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Promise
import scala.concurrent.duration._
import models.CassandraManager

object JsonController extends Controller {

  def getJsonObject(loc: String, d_type: String): JsValue = {

    val (data_a, labels) = CassandraManager.get(loc, d_type)
    val data_b = CassandraManager.get_predicted(loc, d_type)

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
          "data" -> Json.toJson(data_a)
        ),
        Json.obj(
          "label" -> "My Second dataset",
          "fillColor" -> "rgba(151,187,205,0.2)",
          "strokeColor" -> "rgba(151,187,205,1)",
          "pointColor" -> "rgba(151,187,205,1)",
          "pointStrokeColor" -> "#fff",
          "pointHighlightFill" -> "#fff",
          "pointHighlightStroke" -> "rgba(151,187,205,1)",
          "data" -> Json.toJson(data_b)
        )
      )
    )
    return myJson
  }

  def json(loc: String,d_type: String) = Action {
    Ok(getJsonObject(loc, d_type));
  }
}