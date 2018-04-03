## A Bag of Scalatest Goodies

Artem Artemyev

[github.com/foxmk](http://github.com/foxmk)

---
---

Code and slides:

![code and slides link](qrcode.svg)

[https://github.com/foxmk/scalatest-workshop](https://github.com/foxmk/scalatest-workshop)

---
---

Scalatest documentation:

[http://www.scalatest.org/user_guide](http://www.scalatest.org/user_guide)

---
---

## Quick start

---

```scala
// build.sbt

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test" 
```

---

```scala
// UserSpec.scala
class UserSpec extends FlatSpec {
  // Your tests
}

// UserSpec.scala
class UserSpec extends FunSuite {
  // Your tests
}
```

---

```scala
// UserSpec.scala
class UserSpec extends FlatSpec {

  "A User" should "be of age 20" in {
    // your test code
  
  }

}
``` 

---

```scala
// UserSpec.scala
class UserSpec extends FunSuite {
  
  test("User should be of age 20") {
    // your test code
  
  }
  
}
```

---

```scala
// UserSpec.scala
class UserSpec extends FlatSpec {
  
  "A User" should "be of age 20" in {
    val user = User("John")
    assert(user.age == 20) 
  }
  
}
```

---

```text
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

---
---

## Fixtures 

---

- Use Scala to remove duplication
- Use built-in fixture
- Use `BeforeAndAfter*` traits

---

Loan pattern:

```scala
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

---

```scala
class UserSpec extends FlatSpec {

  // fixture code
  
  "A User" should "be cool" in withDatabase { db =>
    val user = db.getUser()
    user shouldBe cool
  }

}
```

---

```scala
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

---

Context trait:

```scala
trait TestFixture {
    
  // Initialize all the stuff
  val a = new A()
  val b = new B()
  val c = new C()
    
}
```

---

```scala
"A User" should "use all the resources" in new TestFixture {
  a.call()
  b.ask()
  c.giveMeSomething()
}
```

---

Scalatest fixture implementation:

```scala
class UserSpec extends FlatSpec {

  override def withFixture(test: NoArgTest) = {
    // Setup fixture
    try {
      super.withFixture(test) // Run the test
    } finally {
      // Do some cleanup
    }
  }
  
  // Your tests
}
```

---

```scala
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

---

```scala
class UserSpec extends FlatSpec with BeforeAndAfter {

  before {
    // do some stuff
  }
  
  after {
    // do some cleanup
  }

  // your test code
  
}
```

---

```scala
class UserSpec extends FlatSpec with BeforeAndAfterAll {
  
  override def beforeEach(): Unit = {
  
    // Do that before each test
    
  }
  
}
```

---

```scala
class UserSpec extends FlatSpec with BeforeAndAfterAll {
  
  override def beforeEach(): Unit = {
    super.beforeEach() // <-- Do not forget this!
    // Do that before each test
    
  }
  
}
```

---

`BeforeAndAfter` traits family:
- `BeforeAndAfter`
- `BeforeAndAfterAll`
- `BeforeAndAfterAllConfigMap`
- `BeforeAndAfterEach`
- `BeforeAndAfterEachTestData`

---
---

## Assertions

---

Assertion functions from `Assertions` trait:

```scala
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

---

```scala
val left = 2 + 2
val right = 5

Predef.assert(left == right)

Assertions.assert(left == right)
```

---

```text
assertion failed
java.lang.AssertionError: assertion failed
	at scala.Predef$.assert(Predef.scala:204)
	at com.github.foxmk.scalatestworkshop.TableDrivenTests.$anonfun$new$1(TableDrivenTests.scala:21)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:12)
	at org.scalatest.OutcomeOf.outcomeOf(OutcomeOf.scala:85)
	at org.scalatest.OutcomeOf.outcomeOf$(OutcomeOf.scala:83)
	at org.scalatest.OutcomeOf$.outcomeOf(OutcomeOf.scala:104)
```

---

```text
4 did not equal 5
ScalaTestFailureLocation: com.github.foxmk.scalatestworkshop.TableDrivenTests at (TableDrivenTests.scala:25)
Expected :5
Actual   :4
```

---

Clues:

```scala
assert(2 + 2 == 5, "2 + 2 still not equal 5")

withClue("2 + 2 still not equal 5") {
  assert(2 + 2 == 5)
}
```

See also:
`ModifiableMessage` and `AppendedClues`

---
---

## Matchers

---

```scala
import org.scalatest.Matchers._
```

---

```scala
true shouldBe true
"Hello, World!" should be("Hello, World!")
Math.PI shouldEqual 3 +- 0.2
```

---

```scala
"foo" shouldBe a [String]
```

---

```scala
List("a", "collection", "of", "strings") should not be empty
List("a", "collection", "of", "strings") should have length 4
List("a", "collection", "of", "strings") should contain ("of")
```

---

```scala
// From documentation
List(1, 2, 2, 3, 3, 3) should contain theSameElementsAs Vector(3, 2, 3, 1, 2, 3)
List(1, 2, 3) should contain atLeastOneOf (2, 3, 4)
List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)
```

---

```scala
val pearlInAShell = DeeplyNestedClass(Foo(Bar(Baz(Quux("eat me!")))))
pearlInAShell should matchPattern { case DeeplyNestedClass(Foo(Bar(Baz(Quux(_))))) => }
```

---

```scala
inside(pearlInAShell) {
  case DeeplyNestedClass(Foo(Bar(Baz(Quux(pearl))))) => pearl.length shouldBe 7
}
```

---

A lot more matchers:
[http://www.scalatest.org/user_guide/using_matchers](http://www.scalatest.org/user_guide/using_matchers) 

---

Inspectors:

```scala
class InspectorsExample extends FunSuite with Inspectors {

  // Your tests
  
  
  
  

}
```

---

Inspectors:

```scala
class InspectorsExample extends FunSuite with Inspectors {
  
  test("collection should have at least 2 even numbers") {
    forAtLeast(2, Seq(1, 2, 3, 4, 5, 6)) { n =>
      n shouldBe even
    }
  }

}
```

---

- `forAll`
- `forAtLeast`
- `forAtMost`
- `forBetween`
- `forEvery`
- `forExactly`

---
---

## Custom matchers

---

```scala
case class Address(street: String, house: String)

case class User(name: String, 
                address: Address, 
                isCool: Boolean)
```

---

```scala
def liveIn(street: String): Matcher[User] = new Matcher[User] {
  override def apply(user: User): MatchResult = {
    MatchResult(user.address.street == street,
                s"User lives in ${user.address.street},
                  but should live in $street",
                s"Indeed, user lives in $street")
  }
}
```

---

```scala
test("User should live in Winterfell") {
  val user = User("Jon Snow", Address("Winterfell", "15"))
  user should liveIn("Winterfell")
}
```

---

```scala
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

---
---

## Property-based testing

---

```scala
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
```

---

```scala
class IDoNotKnowMath extends FlatSpec with PropertyChecks {

  // Your tests





}
```

---

```scala
class IDoNotKnowMath extends FlatSpec with PropertyChecks {

  "Square of any number" should "be even" in {
    forAll("n") { (n: Int) =>
      (n * n) % 2 shouldBe even
    }
  }

}
```

---

```text
TestFailedException was thrown during property evaluation.
  Message: 1 is even
  Location: (TableDrivenTests.scala:19)
  Occurred when passed generated values (
    n = -1 // 12 shrinks
  )
```

---

```scala
val even = new BeMatcher[Int] {
  override def apply(left: Int): MatchResult = {
    MatchResult(left % 2 == 0, s"$left is odd", s"$left is even")
  }
}
```

---

```scala
trait PropertyChecks 
  extends TableDrivenPropertyChecks 
  with GeneratorDrivenPropertyChecks
```

---

Table-driven tests:

```scala
class Fibbonachi extends FlatSpec with TableDrivenPropertyChecks {
  
  // Your tests
  
}
```

---

```scala
val testCases = Table(
  "n" -> "fib(n)",
  1 -> 1,
  2 -> 1,
  3 -> 2,
  4 -> 3,
  5 -> 5,
  6 -> 8
)
```

---

```scala
"fib(n)" should "be n-th Fibbonachi number" in {
  forAll(testCases) { (n, fibN) =>
    fib(n) shoulbBe fibN
  }
}
```

---

Generator-driven tests:

```scala
class IStillDoNotKnowMath extends FlatSpec with GeneratorDrivenPropertyChecks {
  
  // Your tests
  
}
```

---

```scala
import org.scalacheck.Arbitrary._

val squares = arbitrary[Int] map (n => n * n)
```

---

```scala
"Square of any number" should "be even" in {
  forAll(squares) { square =>
    square shouldBe even
  }
}
```

---
---

## Custom generators

---

`org.scalacheck.Gen[A]` is a Monad:
- you can use `map`/`flatMap`
- you can use `for`-comprehension
- you can compose them any way

---

Some examples:

```scala
val mondays = Gen.calendar suchThat (_.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
```

```scala
val nonOverlappingSeqs = for {
  a <- Gen.containerOf[Seq, Int](arbitrary[Int])
  b <- Gen.containerOf[Seq, Int](arbitrary[Int] suchThat (n => !a.contains(n)))
} yield (a, b)
```

---

More examples:

```scala
case class Tweet(content: String)

case class User(name: String, registrationDate: Calendar, tweets: Seq[Tweet])

val tweets = for {
  length   <- Gen.chooseNum(1, 146)
  contents <- Gen.listOfN(length, Gen.alphaNumChar)
} yield Tweet(contents.mkString(""))

val users = for {
  firstName  <- Gen.alphaStr
  lastName   <- Gen.alphaStr
  dateInPast <- Gen.calendar suchThat (_.before(Calendar.getInstance()))
  tweets     <- Gen.containerOf[Seq, Tweet](tweets)
} yield User(firstName + " " + lastName, dateInPast, tweets)
```

---
---

## Creating your own DSL

---

Scala syntactic sugar for method invocation:

```scala
1 + 1 /*is equivalent to*/ 1.+(1)
```

```scala
foo bar /*is equivalent to*/ foo(bar) /*and*/ foo.apply(bar)
```

---

We can use that!

```scala
case class Address(city: String)

case class User(name: String, address: Address)

object MyDsl {
  val city = new {
    def of(user: User): String = user.address.city
  }
}

import MyDsl._

val user = User("Jon Snow", Address("Berlin"))
city of user shouldBe "Berlin"
```

---

```scala
// This line suppresses feature warning
import scala.language.postfixOps

implicit class IntDsl(i: Int) {
  def squared: Int = i * i 
}

(2 squared) shouldBe 4 // Mind the parentheses
```

---

```scala
// This line suppresses implicit conversions warning
import scala.language.implicitConversions

class IntDsl(i: Int) {
  def squared: Int = i * i
}

implicit def toIntDsl(i: Int) = new IntDsl(i)

(2 squared) shouldBe 4 // Mind the parentheses
```

---

```scala
implicit class StringDsl(s: String) {
  def without(c: String): String = s.replaceAll(c, "") 
}

"Foo" without "F" shouldBe "oo"
```

---
---

## Other goodies

---

`*Values` helpers:

```scala
import org.scalatest.OptionValues._
import org.scalatest.TryValues._
import org.scalatest.EitherValues._
import org.scalatest.PartialFunctionValues._
```

---

```scala
None.value shouldBe "foo" // Throws TestFailedException with nice message 
```

equivalent to:

```scala
val i: Option[Int] = None
i.isDefined shouldBe true
i.get shouldBe 1
```

---

```scala
val either: Either[String, Int] = Left("Some error")
either.right.value shouldBe 1
```

```scala
val result: Try[String] = Failure(new Exception)
result.success.value shouldBe "Hello!"
```

---

Private method invocation:

```scala
class GetToGuts extends FunSuite with PrivateMethodTester {

  class Foo {
    private def getMe(n: Int) = "Hello!" * n
  }

  val getMe = PrivateMethod[String]('getMe)

  test("Private method should be accessible now") {
    new Foo() invokePrivate getMe(2) shouldBe "Hello!Hello!"
  }

}
```

---
---

## Tagging tests

---

```scala
import org.scalatest.Tag

object DbTest extends Tag("com.example.testing.DbTest")
```

---

```scala
class UserRepositorySpec extends FlatSpec  {
  
  "A User" should "be in database" taggedAs(DbTest) {
    // Your test
  }
  
}
```

---

Exclude tagged tests:

```text
$ sbt testOnly * -- -l com.example.testing.DbTest
```

Include tagged tests:

```text
$ sbt testOnly * -- -n com.example.testing.DbTest
```

---

- Tags are just... tags
- There are a bunch of predefined tags: `Slow`, `CPU`, `Retryable` etc...

---
---

## Sharing tests

---

```scala
trait UserRepository {
  def getUser(name: String): User
  def saveUser(user: User): User
}

class InMemoryUserRepository extends UserRepository {
  // Some implementation
}

class PostgresUserRepository extends UserRepository {
  // Some implementation
}
```

---

```scala
trait UserRepositoryBehaviour { this: FlatSpec =>

  def statefulUserRepository(repository: => UserRepository) {

    it should "save and get user" in {
      val user = User()
      repository.saveUser(user)
      repository.getUser(user.name) shouldBe user
    }
    
    // More tests...
  }
}
```

note: talk about self-type annotations

---

```scala
class InMemoryUserRepositorySpec extends FlatSpec with UserRepositoryBehaviour {

  "InMemoryUserRepository" should behave like statefulUserRepository(new InMemoryUserRepository())
}
```

```scala
class PostgresUserRepositorySpec extends FlatSpec with UserRepositoryBehaviour {

  "PostgresUserRepository" should behave like statefulUserRepository(new PostgresUserRepository())
}
```

---
---

## Selenium DSL

---

```scala
libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.35.0" % "test"
```

---

```scala
class MyPageSpec extends FlatSpec with Chrome {

  // Your tests
  
  
  
  
}
```

---

```scala
class MyPageSpec extends FlatSpec with Chrome {

  "Example page" should "have example title" in {
    go to "http://example.com"
    pageTitle shouldBe "Example page"
  }
  
}
```

---

Available browsers:
- `Chrome`
- `Safari`
- `InternetExplorer`
- `Firefox`
- `Firefox`

---

Navigation:
- `go to "http://example.com"`
- `click on "superButton"` (uses id or name)
- `textField("login").value = "jon.snow@winterfell.com"`
- `goBack()`
- `goForward()`
- `reloadPage()`
- `add cookie ("cookie_name", "cookie_value")`
- `capture to "screenshot_001.png"`
- `close`
- `quit`
- etc.

---

Selectors:

```scala
click on cssSelector(".main-menu .sections .home-page")
```

- id
- name
- xpath
- className
- cssSelector
- linkText
- partialLinkText
- tagName


---

Implicit timeouts:

```scala
implicitlyWait(Span(10, Seconds))
```

---

More in documentation: [http://www.scalatest.org/user_guide/using_selenium](http://www.scalatest.org/user_guide/using_selenium)

---
---

## Lesson

Be brave and explore documentation!

Lots of good stuff there, it's not a Pandora's box!

---
---

## Thank you!

Code and slides:

![code and slides link](qrcode.svg)

[https://github.com/foxmk/scalatest-workshop](https://github.com/foxmk/scalatest-workshop)
