package com.github.foxmk.scalatestworkshop

import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.matchers._

class CustomMatchers extends FunSuite {

  case class Address(street: String, house: String)

  case class User(name: String, address: Address, isCool: Boolean)

  // You can put `Matcher[T]` (`BeMatcher[T]`) on the right-hand side of `should` (`shouldBe`/`should be`)
  // when matching object of type `T`.
  //
  // `liveIn` is just a function returning a matcher, nothing more.
  //
  def liveIn(street: String): Matcher[User] = new Matcher[User] {
    override def apply(user: User): MatchResult = {
      MatchResult(user.address.street == street,
                  s"User lives in ${user.address.street}, but should live in $street",
                  s"Indeed, user lives in $street")
    }
  }

  test("User should live in Winterfell") {
    User("Jon Snow", Address("Winterfell", "15"), isCool = true) should liveIn("Winterfell")
  }

  val cool: BeMatcher[User] = { user =>
    MatchResult(user.isCool, s"User is not cool, nope :(", s"Indeed, user is cool")
  }

  // Code above is equivalent to:
  //
  //  val cool: BeMatcher[User] = new BeMatcher[User] {
  //    override def apply(user: User): MatchResult = {
  //      MatchResult(user.isCool, s"User is not cool, nope :(", s"Indeed, user is cool")
  //    }
  //  }
  //
  // This trick uses "single abstract method" syntactic sugar, which allows to replace
  // implementation of interface with one method with partial function

  test("User should be cool") {
    User("Jon Snow", Address("Winterfell", "15"), isCool = true) shouldBe cool
  }
}
