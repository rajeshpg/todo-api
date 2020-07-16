package todo

import common.{SecuredController, SecuredControllerComponents}
import javax.inject.Inject
import play.api.libs.json._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TodoController @Inject()(controllerComponents: SecuredControllerComponents, todoRepository: TodoRepository) extends SecuredController(controllerComponents) {

  val todos: mutable.Map[String, List[Todo]] = mutable.Map.empty[String, List[Todo]]

  def create = AuthenticatedAction(parse.json).async { implicit request =>
    val todoJson = request.body.validate[Todo]
    todoJson.fold(
      errors => {
        println(errors)
        Future(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      todo =>
        todoRepository.save(todo).map(result => Created(Json.toJson(todo)))
    )
  }

  def getTodoForUser = AuthenticatedAction.async { implicit request =>
    val userName = request.userSession.userName
    todoRepository.fetchByUserName(userName).map(todos => Ok(Json.toJson(todos)))
  }

}
