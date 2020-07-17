package todo

import javax.inject.Inject
import org.xbill.DNS.SMIMEARecord.Selector
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.play.json._

class TodoRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) {



  def todosCollection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("todos"))

  def save(todo: Todo): Future[WriteResult] ={
    todosCollection.flatMap(_.insert(false).one(todo))
  }

  def fetchByUserName(userName: String): Future[Seq[Todo]] = {
    todosCollection.flatMap(_.find(
      selector = Json.obj(/* Using Play JSON */),
      projection = Option.empty[JsObject])
      .sort(Json.obj("_id"-> -1))
      .cursor[Todo](ReadPreference.primary)
      .collect[Seq](10, Cursor.FailOnError[Seq[Todo]]()))
  }

  def fetchAfter(userName: String, id: String) = {
    val _id =  BSONObjectID.parse(id)
    val document = BSONDocument("_id" -> BSONDocument("$lt" -> _id.get))
    execQuery(document)
  }

  def fetchBefore(userName: String, id: String) = {
    val _id =  BSONObjectID.parse(id)
    val document = BSONDocument("_id" -> BSONDocument("$gt" -> _id.get))
    execQuery(document)
  }

  private def execQuery(queryDocument: BSONDocument) = {
    todosCollection.flatMap(_.find(
      selector = queryDocument,
      projection = Option.empty[BSONDocument])
      .sort(Json.obj("_id"-> -1))
      .cursor[Todo](ReadPreference.primary)
      .collect[Seq](5, Cursor.FailOnError[Seq[Todo]]()))
  }

}
