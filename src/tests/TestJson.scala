package tests

import org.scalatest._
import play.api.libs.json.{JsValue, Json}


class TestJson extends FunSuite {


  test("Testing Json") {
    val map: Map[String, String] = Map(
      "username" -> "focacci",
      "action" -> "connected"
    )

    val json_map = Json.stringify(Json.toJson(map))

    val test: JsValue = Json.parse(json_map)
    val username: String = (test \ "username").as[String]
    val action: String = (test \ "action").as[String]

    assert(username === "focacci")
    assert(action === "connected")
  }


}
