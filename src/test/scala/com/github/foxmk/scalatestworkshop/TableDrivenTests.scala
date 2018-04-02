package com.github.foxmk.scalatestworkshop

import org.scalatest.{Assertions, FlatSpec, Inspectors}
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.Matchers._
import org.scalatest.matchers.{BeMatcher, MatchResult}

class TableDrivenTests extends FlatSpec with GeneratorDrivenPropertyChecks with Inspectors {

  val odd = new BeMatcher[Int] {
    override def apply(left: Int): MatchResult = {
      MatchResult(left % 2 == 0, s"$left is even", s"$left is odd")
    }
  }

  "Square of any number" should "be odd" in {
//    forAll("n") { (n: Int) =>
//      (n * n) % 2 shouldBe odd
//    }

    forAtLeast(2, Seq(1, 2, 3, 4)) { n =>
      n shouldBe odd
    }

  }

}
