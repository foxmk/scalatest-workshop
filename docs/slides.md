## scalatest

note: Hi, my name is Artem and today I want to talk about scalatest.

---

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

Custom setup and/or teardown?

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
  val res = code(db)
  db.close()
  res
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
  val res = code(a, b, c)
  a.close()
  a.destroy()
  a.eliminate()
  res
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

note: You simply wrap tour test code in this trait et voila.

---

note:. there is one problem though, it is hard to define teardown in this case

---

note:. well, scalatest offers its own way to create fixtures. you need to extend your desired test style from fixtures package

---

note:. and define fixture class and construction function. kinda like built in loan pattern on steroids

---

note:. here we create case class will require fixture elements and override with fixture method

---

note:. now let’s take a look at our assertions

---

note:. yuk, that’s ugly

---

note:. fortunately scalatest provide an extensive dsl for mathibg stuff

---

note:. you can make assertions about simple values

---

note:. classes

---

note:. collections

---

note:. more collections

---

note:. well with a deeeply nested class that is not so pretty 

---

note:. what if we don’t care about concrete value inside?

---

note:. we can use match pattern

---

note:. if we do care, we can mix in inside trait which provides inside method. we can use it to traverse deeply nested class to make sure it has required properties

---

note:. there are a lot of marchers , you can take a look at documentation to have some impression of what you can achieve 

---

note:. i want to show how you can crate tour own marchers 

---

note:. let’s have a user

---

note:. say we want to be sure that user lives in certain street

---

note:. we create a matched which accepts user on lhs and street name on rhs.

---

note:. it checks the street dnd returns pretty message

---

note:. now we can use it

---

note:. there are also be marchers and have marchers. you can define them to make your assertion more like human language 

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

