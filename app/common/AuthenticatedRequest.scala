package common

import play.api.mvc.{Request, WrappedRequest}

class AuthenticatedRequest[A](val userSession: UserSession, request: Request[A]) extends WrappedRequest[A](request)
