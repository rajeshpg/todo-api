package common

import java.time.Clock

import javax.inject.Inject
import play.api.Configuration
import play.api.libs.typedmap.TypedKey
import play.api.mvc._
import pdi.jwt.JwtSession._

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedActionBuilder @Inject()(playBodyParsers: PlayBodyParsers)
                                          (implicit val executionContext: ExecutionContext, conf:Configuration)
  extends ActionBuilder[AuthenticatedRequest, AnyContent] with Results {

  override def parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  implicit val clock: Clock = Clock.systemUTC

  override def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]): Future[Result] = {
    request.jwtSession.getAs[UserSession]("user") match {
      case Some(user) =>
        request.addAttr(TypedKey("user"), user)
        block(new AuthenticatedRequest[A](user, request)).map(_.refreshJwtSession(request))
      case _ =>
        Future(Unauthorized)
    }
  }

}
