sscalatest

note: Hi, my name is Artem and today I want to talk about scalatest

---

note: Disclaimer: everything I will be talking about is in the documentation

---

note: I assume most of you already know how to use scalatest, but just in case you don’t here’s quickstart

---

note: add dependency in your build sbt 

---

note: create a test class extending one of test suites

---

note: scalatest provides different test styles which affect how you write and describe your tests

---

note: I will use flatspec, which is defacto default and fun suite which is the simplest

---

note: then you write some tests

---

note: and run them with abt test, simple enough 

---

note:. but what if you need to have custom setup and teardown for your tests? 

---

note:. Of course you can mix in predefined trait befor and after each and before and after all

---

note:. and define your setup and test down code

---

note:. you can override predefined methods or use them as is. sometimes you want custom setup for each test

---

note:. For that of course you can use loan pattern. This is a function which accepts some fixture an block of code which uses it. creates the fixture, rubs the code, saves result, performs tear down and returns the result back to caller

---

note:. If you have more elements in fixture loan pattern becomes tedious 

---

note:. Fixture trait to the rescue. here we create a trait or abstract class which body is the test body. body will be executed upon construction of an object

---

note:. you simply erap tour test code in this trait et voila

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

