package controllers

import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import models.CassandraManager

object JsonController extends Controller {

  def getJsonObject(loc: String, d_type: String): JsValue = {

    val (data_a, labels) = CassandraManager.get(loc, d_type)
    val data_b = CassandraManager.get_predicted(loc, d_type)

    val myJson: JsValue = Json.obj(
      "labels" -> labels,
      "sensor_data" -> data_a,
      "predicted_data" -> data_b
    )
    return myJson
  }

  def json(loc: String,d_type: String) = Action {
    Ok(getJsonObject(loc, d_type));
  }
}