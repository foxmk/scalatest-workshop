package com.github.foxmk.scalatestworkshop

import org.scalatest.FlatSpec

class UserSpec extends FlatSpec {

  case class User(name: String) {
    val age = 20
  }

  "A User" should "be of age 20" in {
    val user = User("John")
    assert(user.age == 20)
  }
}
