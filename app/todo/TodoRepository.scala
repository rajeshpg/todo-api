package todo

import javax.inject.Inject
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.play.json._

class TodoRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) {

  def todosCollection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("todos"))

  def save(todo: Todo): Future[WriteResult] ={
    todosCollection.flatMap(_.insert(false).one(todo))
  }

  def fetchByUserName(userName: String) = {
    todosCollection.flatMap(_.find(
      selector = Json.obj(/* Using Play JSON */),
      projection = Option.empty[JsObject])
      .cursor[Todo](ReadPreference.primary)
      .collect[Seq](10, Cursor.FailOnError[Seq[Todo]]()))
  }

}
