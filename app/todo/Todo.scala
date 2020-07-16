package todo

import java.time.Instant

import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._
case class Todo(_id: Option[BSONObjectID], userName: String, description: String, targetDate: Instant, completed: Option[Boolean] = Some(false))
object Todo{
  implicit val todoFormat: OFormat[Todo] = Json.format[Todo]
}
