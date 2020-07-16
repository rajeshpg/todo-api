package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.{FakeRequest, Injecting}
import play.api.test.Helpers._
import play.api.libs.json._

class LoginControllerTest extends PlaySpec with GuiceOneAppPerTest with Injecting  {
  "LoginController auth " should{
    "authenticate user with username and password" in {
      val payload = Json.parse(
        """
          |{
          |"username":"rajesh",
          |"password":"secret"
          |}
          |""".stripMargin)
      val response = route(app, FakeRequest(POST, "/auth").withJsonBody(payload)).get
      println(headers(response))

      status(response) mustBe OK
    }
  }

}
