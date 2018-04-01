# `scalatest`

note: Hi, my name is Artem and today I want to talk about scalatest.

---

Scalatest documentation:
[http://www.scalatest.org/user_guide](http://www.scalatest.org/user_guide)

note: Disclaimer: everything I will be talking about is in the documentation.

---

## Quick start

note: I assume most of you already know how to use scalatest, but just in case you don’t here’s quickstart.

---

```
// build.sbt

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test" 
```

note: Add dependency in your build sbt.

---

```
// UserSpec.scala
class UserSpec extends FlatSpec {
  // Your tests
}

// UserSpec.scala
class UserSpec extends FunSuite {
  // Your tests
}
```

notes: Create a test class extending one of test suites. scalatest provides different test styles which affect how you write and describe your tests. 

---

```
// UserSpec.scala
class UserSpec extends FlatSpec {

  "A User" should "be of age 20" in {
    // your test code
  
  }

}
```

note: I will use `FlatSpec`, which is defacto default 

---

```
// UserSpec.scala
class UserSpec extends FunSuite {
  
  test("User should be of age 20") {
    // your test code
  
  }
  
}
```

note: and `FunSuite` which is the simplest one.

---

```
// UserSpec.scala
class UserSpec extends FlatSpec {
  
  "A User" should "be of age 20" in {
    val user = User("John")
    assert(user.age == 20) 
  }
  
}
```

note: Then you write some tests

---

```
$ sbt test
...
[info] UserSpec:
[info] A User
[info] - should be of age 20
[info] Run completed in 100 milliseconds.
[info] Total number of tests run: 1
[info] All tests passed.
...
```

note: and run them with sbt test, simple enough.

---

## Fixtures

note: But what if you need to have custom setup and teardown for your tests? 

---

```
class UserSpec extends FlatSpec with BeforeAndAfterAll {

  // your test code
  
  
  
  
  
}
```

note: Of course you can mix in predefined trait befor and after each and before and after all

---

```
class UserSpec extends FlatSpec with BeforeAndAfterAll {
  
  beforeEach {
  
    // Do that before each test
    
  }
  
}
```

note: and define your setup and test down code. You can use predefined methods

---

```
class UserSpec extends FlatSpec with BeforeAndAfterAll {
  
  override def beforeEach(): Unit = {
  
    // Do that before each test
    
  }
  
}
```

note: ...or override them.

---

```
class UserSpec extends FlatSpec with BeforeAndAfterAll {
  
  override def beforeEach(): Unit = {
    super.beforeEach() // <-- Do not forget this!
    // Do that before each test
    
  }
  
}
```

note: Be careful with inheritance. Sometimes you want custom setup for each test.

---

```
def withDatabase[A](db: => DB)(code: DB => A): A = {
  // Create fixture
  val db = new DB()
  try {
    code(db)
  } finally {
    db.close()
  }
}
```

note:. For that of course you can use loan pattern. This is a function which accepts some fixture factory
and a block of code which uses it. Creates the fixture, runs the code, saves result, performs tear down and returns the result back to caller.

---

```
def withAllTheStuff[ResultT](a: => A)(b: => B)(c: => C)(code: (A, B, C) => ResultT): ResultT = {
  // Create all the stuff
  val a = new A()
  val b = new B()
  val c = new C()
  try {
    code(a, b, c)
  } finally {
    a.close()
    b.destroy()
    c.eliminate()
  }
}
```

note:. If you have more elements in fixture loan pattern becomes tedious.

---

```
trait TestFixture {
    
  // Initialize all the stuff
  val a = new A()
  val b = new B()
  val c = new C()
    
}
```

note:. Fixture trait to the rescue. Here we create a trait or abstract class which body is the test body.
Body will be executed upon construction of an object.

---

```
"A User" should "use all the resources" in new TestFixture {
  a.call()
  b.ask()
  c.giveMeSomething()
}
```

note: You simply wrap tour test code in this trait et voila. there is one problem though, it is hard to define teardown in this case

---

```
class UserSpec extends fixture.FlatSpec {


  // Your test code
    

}
```

note:. well, scalatest offers its own way to create fixtures. you need to extend your desired test style from fixtures package

---

```
class UserSpec extends fixture.FlatSpec {

  case class TestFixture(a: A, b: B)
  
  def withFixture(test: OneArgTest) = {
    val f = TestFixture(new A(), new B())
    
    try {
      withFixture(test.toNoArgTest(f))
    } finally {
      a.close()
      b.destroy()
      c.eliminate()
    }
  }

}
```

note: and define fixture class and construction function. kinda like built in loan pattern on steroids. Here we create case class will require fixture elements and override with fixture method

---

> Mix in a before-and-after trait when you want an aborted suite, not a failed test, if the fixture code fails.

note: fixture params cannot cancel test, only fail

---

## Assertions and Matchers

note:. now let’s take a look at our assertions

---

Assertion functions from `Assertions` trait:
```
assert(2 + 2 == 5)
assertResult(5) { 2 + 2 }
assertThrows { "I shall not throw" }
val throwed = intercept { "I will not throw also" }
assertCompiles("(function(){alert(\"You've been PWNED!\")})();")
assertTypeError("")
assume(Math.PI == 3) // Will cancel instead of fail

// unconditional results:
fail
cancel
succeed
```

note: There are a bunch of assertions in Assertions trait

---

Clues:
```
assert(2 + 2 == 5, "2 + 2 still not equal 5")
```

See also:
`ModifiableMessage` and `AppendedClues`

---

import org.scalatest.Matchers._

note:. fortunately scalatest provide an extensive dsl for mathibg stuff

---

```
true shouldBe true
"Hello, World!" should be("Hello, World!")
Math.PI shouldEqual 3 +- 0.2
```

note:. you can make assertions about simple values

---

```
"foo" shouldBe a [String]
```

note:. classes

---

```
List("a", "collection", "of", "strings") should not be empty
List("a", "collection", "of", "strings") should have length 4
List("a", "collection", "of", "strings") should contain ("of")
```

note:. collections

---

```
// From documentation
List(1, 2, 2, 3, 3, 3) should contain theSameElementsAs Vector(3, 2, 3, 1, 2, 3)
List(1, 2, 3) should contain atLeastOneOf (2, 3, 4)
List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)
```

note:. more collections

---

```
val pearlInAShell = DeeplyNestedClass(Foo(Bar(Baz(Quux("eat me!")))))
pearlInAShell should matchPattern { case DeeplyNestedClass(Foo(Bar(Baz(Quux(_))))) => }
```

note: what if we don’t care about concrete value inside? we can use match pattern

---

```
inside(pearlInAShell) {
  case DeeplyNestedClass(Foo(Bar(Baz(Quux(pearl))))) => pearl.length shouldBe 7
}
```

note: if we do care, we can mix in inside trait which provides inside method. we can use it to traverse deeply nested class to make sure it has required properties

---

A lot more matchers:
[http://www.scalatest.org/user_guide/using_matchers](http://www.scalatest.org/user_guide/using_matchers)

note:. there are a lot of marchers , you can take a look at documentation to have some impression of what you can achieve 

---

## Custom matchers

---

```
case class Address(street: String, house: String)

case class User(name: String, address: Address, isCool: Boolean)
```

note: I want to show how you can crate tour own marchers. let’s have a user

---

```
def liveIn(street: String): Matcher[User] = new Matcher[User] {
  override def apply(user: User): MatchResult = {
    MatchResult(user.address.street == street,
                s"User lives in ${user.address.street},
                  but should live in $street",
                s"Indeed, user lives in $street")
  }
}
```

note: Say we want to be sure that user lives in certain street. We create a matched which accepts user on lhs and street name on rhs. It checks the street dnd returns pretty message.

---

```
test("User should live in Winterfell") {
  val user = User("Jon Snow", Address("Winterfell", "15"))
  user should liveIn("Winterfell")
}
```

note: Now we can use it

---

```
val cool: BeMatcher[User] = { user =>
  MatchResult(
    user.isCool, 
    s"User is not cool, nope :(", s"Indeed, user is cool"
  )
}

test("User should be cool") {
  val user = User("Jon Snow", isCool = true)
  user shouldBe cool
}
```
  
note: There are also be marchers and have marchers. You can define them to make your assertion more like human language 

---

note:. of course this is all useful if we have some test cases . but who likes to think of test cases

---

note:. let’s have machine do it for us

---

note:. ....

---

note:. scalatest also provides nice selenium dsl

---

note:. 

---



---

