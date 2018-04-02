package com.github.foxmk.scalatestworkshop

import org.scalatest.{Assertions, FunSuite, Inside, Inspectors}
import org.scalatest.Matchers._

class MatchingPattern extends FunSuite with Inside with Assertions with Inspectors {

  // Sometimes you need to work with deeply nested classes:

  case class Quux[T](string: T)
  case class Baz[T](quux: Quux[T])
  case class Bar[T](baz: Baz[T])
  case class Foo[T](bar: Bar[T])
  case class DeeplyNestedClass[T](foo: Foo[T])

  val example = DeeplyNestedClass(Foo(Bar(Baz(Quux("eat me!")))))

  test("Matching exatly") {
    example shouldBe DeeplyNestedClass(Foo(Bar(Baz(Quux("eat me!")))))
  }

  // But what if you don't care about exact value?

  test("Don't care about contents, gimme structure") {
    // Looks a bit ugly :( BTW, empty body is required
    example should matchPattern { case DeeplyNestedClass(Foo(Bar(Baz(Quux(_))))) => }

    // This can match anything that normal pattern matching. Use `unapply`, Luke!
  }

  // Sometimes you need more elaborate checks, `Inside` trait can help:

  test("Checking something `inside`") {
    // Comes from `Inside` trait
    inside(example) {
      case DeeplyNestedClass(Foo(Bar(Baz(Quux(pearl))))) => pearl.length shouldBe 7
    }

    forAll(Seq("a", "b")) { n =>
      n == "a"
    }

  }

  test("Can even nest `inside` in `inside`") {
    inside(example) {
      case DeeplyNestedClass(foo) =>
        inside(foo) {
          case Foo(Bar(baz)) =>
            inside(baz) {
              case Baz(Quux(pearl)) =>
                "I'm tired, don't care anymore..."
            }
        }
    }
  }
}
