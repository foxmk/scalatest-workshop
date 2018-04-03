package com.github.foxmk.scalatestworkshop
import org.scalatest.Matchers._
import org.scalatest.FlatSpec

import scala.language.implicitConversions

class CustomDsl extends FlatSpec {

  case class Address(city: String)

  case class User(name: String, address: Address)

  object MyDsl {

    val city = new {
      def of(user: User): String = user.address.city
    }
  }

  import MyDsl._

  "A User" should "live in Berlin" in {
    val user = User("Jon Snow", Address("Berlin"))
    city of user shouldBe "Berlin"
  }
}
