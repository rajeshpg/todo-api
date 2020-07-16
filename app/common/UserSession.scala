package common

import play.api.libs.json.Json

case class UserSession(userName: String)
object UserSession{
  implicit val format = Json.format[UserSession]
}
