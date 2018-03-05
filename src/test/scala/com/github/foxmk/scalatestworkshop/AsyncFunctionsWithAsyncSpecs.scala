package com.github.foxmk.scalatestworkshop

import org.scalatest.AsyncFunSuite
import org.scalatest.Matchers._

import scala.concurrent.Future

class AsyncFunctionsWithAsyncSpecs extends AsyncFunSuite {

  case class User(id: String, name: String)

  class UserRepository {
    def getUser(id: String): Future[Option[User]] = id match {
      case "valid"        => Future.successful(Some(User(id = "valid", name = "A Perfectly Valid Book")))
      case "non_existing" => Future.successful(None)
      case _              => Future.failed(new Exception("Invalid book"))
    }
  }

  val repository = new UserRepository

  test("Repository test with `whenReady`") {
    repository.getUser("valid").map { maybeBook =>
      maybeBook shouldBe Some(User(id = "valid", name = "A Perfectly Valid Book"))
    }
  }

}
