package com.github.foxmk.scalatestworkshop

import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future

class AsyncFunctionsWithScalaFutures extends FunSuite with ScalaFutures {

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
    whenReady(repository.getUser("valid")) { maybeBook =>
      maybeBook shouldBe Some(User(id = "valid", name = "A Perfectly Valid Book"))
    }
  }

  test("Repository test with `.futureValue`") {
    val maybeBook = repository.getUser("valid").futureValue
    maybeBook shouldBe Some(User(id = "valid", name = "A Perfectly Valid Book"))
  }

}
