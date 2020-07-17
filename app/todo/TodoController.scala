package todo

import common.{SecuredController, SecuredControllerComponents}
import javax.inject.Inject
import play.api.libs.json._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TodoController @Inject()(controllerComponents: SecuredControllerComponents, todoRepository: TodoRepository) extends SecuredController(controllerComponents) {

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
    println("fetch for user "+userName)
    todoRepository.fetchByUserName(userName).map(todos => Ok(Json.toJson(todos)))
  }

  def getTodosAfter = AuthenticatedAction.async { implicit request =>
    val userName = request.userSession.userName
    val id: Option[String] = request.getQueryString("id")

    id match {
      case None => Future(BadRequest("id is required"))
      case Some(id) => todoRepository.fetchAfter(userName, id).map(todos => Ok(Json.toJson(todos)))
    }
  }

    def getTodosBefore = AuthenticatedAction.async { implicit request =>
      val userName = request.userSession.userName
      val id: Option[String] = request.getQueryString("id")

      id match {
        case None => Future(BadRequest("id is required"))
        case Some(id) => todoRepository.fetchBefore(userName, id).map(todos => Ok(Json.toJson(todos)))
      }
    }

}
