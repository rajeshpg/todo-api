package todo

import java.time.Instant

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsObject, Json}
import play.api.test.Helpers._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TodoRepositoryTest extends PlaySpec with GuiceOneAppPerSuite with BeforeAndAfterAll {


  override def fakeApplication = new GuiceApplicationBuilder()
    .configure(
      "mongodb.uri" -> "mongodb://localhost:27017/todos-test"
    )
    .build()

  lazy val reactiveMongoApi = fakeApplication.injector.instanceOf[ReactiveMongoApi]

  val todoRepository = fakeApplication.injector.instanceOf[TodoRepository]
  lazy val todos: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("todos"))

  "TodoRepository " should {
    "save a given todo " in {
      val todo = Todo(userName = "test app", description = "make this test pass", targetDate = Instant.now, completed = Some(false), _id = None)
     val result =  await(todoRepository.save(todo))
      result.n mustBe 1
    }

    "fetch all todos of a user " in {
      val todos: Seq[Todo] = await(todoRepository.fetchByUserName("test app"))
      println(todos)
    }
  }

  override def afterAll(): Unit = {
    todos.flatMap(_.drop(failIfNotFound = false))
  }

}
