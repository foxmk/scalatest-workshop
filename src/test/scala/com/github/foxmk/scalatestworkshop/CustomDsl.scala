package com.github.foxmk.scalatestworkshop
import org.scalatest.Matchers._
import org.scalatest.{FlatSpec, PrivateMethodTester}
import org.scalatest.OptionValues._
import org.scalatest.TryValues._
import org.scalatest.EitherValues._
import org.scalatest.PartialFunctionValues._

import scala.language.implicitConversions

class CustomDsl extends FlatSpec with PrivateMethodTester {

  case class Address(city: String)

  case class User(name: String, address: Address)

  object MyDsl {

    val city = new {
      def of(user: User): String = user.address.city
    }

    implicit class StringDsl(s: String) {
      def without(c: String): String = s.replaceAll(c, "")
    }

    class IntDsl(i: Int) {
      def squared: Int = i * i
    }

    implicit def toIntDsl(i: Int) = new IntDsl(i)
  }

  import MyDsl._

  "A User" should "live in Berlin" in {
    val user = User("Jon Snow", Address("Berlin"))
    city of user shouldBe "Berlin"
  }

  it should "dasfd" in {
//    val i: Either[Int, String] = Right("foo")
//    i.left.value shouldBe 1
//    i.leftSideValue shouldBe 1
    class Foo {
      private def getMe(n: Int) = "Hello!" * n
    }

    val getMe = PrivateMethod[String]('getMe)

    new Foo() invokePrivate getMe(2) shouldBe "Hello!Hello!"
  }

}
