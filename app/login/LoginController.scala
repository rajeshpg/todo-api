package login

import java.time.Clock

import common.{SecuredController, SecuredControllerComponents, UserSession}
import javax.inject.Inject
import play.api.Configuration
import play.api.libs.functional.syntax._
import play.api.libs.json._
import pdi.jwt.JwtSession._

class LoginController @Inject()(scc: SecuredControllerComponents)
                               (implicit val conf: Configuration)
  extends SecuredController(scc) {

    implicit val clock: Clock = Clock.systemUTC

  def auth = Action(parse.json) { implicit request =>
    val loginForm: Reads[(String, String)] =
      ((JsPath \ "username").read[String] and (JsPath \ "password").read[String]).tupled

    request.body
      .validate(loginForm)
      .fold(
        errors => {
          BadRequest(JsError.toJson(errors))
        }, {
          case (username, _) => Ok(Json.toJson(UserSession(username))).addingToJwtSession("user", UserSession(username))
        }
      )

  }
}
