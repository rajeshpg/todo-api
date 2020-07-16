package controllers

import java.time.Clock

import common.UserSession
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import pdi.jwt.{JwtAlgorithm, JwtHeader, JwtSession}
import play.api.test.{FakeHeaders, FakeRequest, Injecting}
import play.api.test.Helpers._
import play.api.libs.json._
import pdi.jwt.JwtSession._
import play.api.Configuration


class TodoControllerTest extends PlaySpec with GuiceOneAppPerTest with Injecting  {
  implicit val clock: Clock = Clock.systemUTC
  implicit lazy val conf:Configuration = app.configuration

  "TodoController " should {

    "create a todo for a given user" in {
      val payload = Json.parse(
        """
          |{
          |"userName":"rajesh",
          |"description": "complete this test",
          |"targetDate": "2020-07-16T17:30:32.150541Z"
          |}
          |""".stripMargin)
      val response = route(app, FakeRequest(POST, "/todos").withJsonBody(payload)).get
      status(response) mustBe CREATED
      contentAsString(response) mustBe payload.toString()
    }

    "return list of todos" in {
      val user = new JsObject(Map("user" -> Json.toJson(UserSession("test app"))))
      val session: JwtSession = JwtSession(user).withHeader(JwtHeader(JwtAlgorithm.HS256))
      println(session)
      val payload1 = Json.parse(
        """
          |{
          |"userName":"rajesh",
          |"description": "complete this test",
          |"targetDate": "2020-07-16T17:30:32.150541Z"
          |}
          |""".stripMargin)
      val p1r1 = route(app, FakeRequest(POST, "/todos").withJsonBody(payload1).withHeaders(("Authorization", s"Bearer ${session.serialize}"))).get
      status(p1r1) mustBe CREATED
      val payload2 = Json.parse(
        """
          |{
          |"userName":"rajesh",
          |"description": "write more test",
          |"targetDate": "2020-07-16T18:30:32.150541Z"
          |}
          |""".stripMargin)
      val p2r2 = route(app, FakeRequest(POST, "/todos").withJsonBody(payload1).withHeaders(("Authorization", s"Bearer ${session.serialize}"))).get
      status(p2r2) mustBe CREATED

      val fakeRequest = FakeRequest(GET, "/user/todos")
        .withHeaders(("Authorization", s"Bearer ${session.serialize}"))
      val response = route(app, fakeRequest).get
      status(response) mustBe OK
    }
  }

}
