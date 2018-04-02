Hi, my name is Artem and today I want to talk about scalatest.

Disclaimer: everything I will be talking about is in the documentation.

I assume most of you already know how to use scalatest, but just in case you don’t here’s quickstart.
Add dependency in your build sbt.
I will use `FlatSpec`, which is defacto default and `FunSuite` which is the simplest one.
Then you write some tests and run them with sbt test, simple enough.

But what if you need to have custom setup and teardown for your tests?
Of course you can mix in predefined trait before and after each and before and after all
and define your setup and test down code. You can use predefined methods
...or override them.
Be careful with inheritance. Sometimes you want custom setup for each test.
 For that of course you can use loan pattern. This is a function which accepts some fixture factory
If you have more elements in fixture loan pattern becomes tedious.
Fixture trait to the rescue. Here we create a trait or abstract class which body is the test body.
You simply wrap tour test code in this trait et voila. there is one problem though, it is hard to define teardown in this case
Well, scalatest offers its own way to create fixtures. You need to extend your desired test style from fixtures package and define fixture class and construction function. kinda like built in loan pattern on steroids. Here we create case class will require fixture elements and override with fixture method
fixture params cannot cancel test, only fail

Now let’s take a look at our assertions
There are a bunch of assertions in Assertions trait
 fortunately scalatest provide an extensive dsl for mathibg stuff
 you can make assertions about simple values
 classes
 collections
 more collections
what if we don’t care about concrete value inside? we can use match pattern
if we do care, we can mix in inside trait which provides inside method. we can use it to traverse deeply nested class to make sure it has required properties
 there are a lot of marchers , you can take a look at documentation to have some impression of what you can achieve
I want to show how you can crate tour own marchers. let’s have a user
Say we want to be sure that user lives in certain street. We create a matched which accepts user on lhs and street name on rhs. It checks the street dnd returns pretty message.
Now we can use it
There are also be marchers and have marchers. You can define them to make your assertion more like human language

## Property checks

Of course this is all useful if we have some test cases. but who likes to think of test cases, let’s have machine do it for us
shrinking
