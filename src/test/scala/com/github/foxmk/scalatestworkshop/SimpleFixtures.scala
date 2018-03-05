package com.github.foxmk.scalatestworkshop

import org.scalatest.{Assertion, FunSuite}
import org.scalatest.Matchers._

class SimpleFixtures extends FunSuite {

  case class User(name: String)

  trait DB {
    def getUsers: Seq[User]
  }

  case class InMemoryDB(users: Seq[User]) extends DB {
    override def getUsers: Seq[User] = users
  }

  // Sometimes you need to share custom initialization between tests.
  // The simplest way to do that is wrap initialization in fixture function, aka "Loan pattern":

  def withUsers(users: Seq[User])(test: DB => Assertion): Assertion = {
    // Initialize stuff
    val db = InMemoryDB(users)

    // Run test
    val result = test(db)

    // Deinitialize
    //   db.kill()

    result
  }

  // Just wrap your test code in fixture:

  test("Using fixture function") {
    withUsers(Seq(User("Jon Snow"))) { db =>
      db.getUsers should not be 'empty
    }
  }

  // But if you need to provide more than one object, that could be messy. Use fixture trait!

  trait TestFixture {
    // You can override stuff for some tests
    def users: Seq[User] = Seq.empty

    val db1 = new DB {
      override def getUsers: Seq[User] = users
    }

    val db2 = new DB {
      override def getUsers: Seq[User] = users.map(user => user.copy(name = user.name.toUpperCase()))
    }
  }

  // Use the same way

  test("Using fixture trait") {
    new TestFixture {
      override def users: Seq[User] = Seq(User("Jon Snow"))

      db1.getUsers should not be 'empty
      db2.getUsers should not be 'empty
    }
  }

  // `TestFixture` can also be an abstract class, if you want:

  abstract class AnotherTestFixture(users: Seq[User]) {
    val db1 = new DB {
      override def getUsers: Seq[User] = users
    }

    val db2 = new DB {
      override def getUsers: Seq[User] = users.map(user => user.copy(name = user.name.toUpperCase()))
    }
  }

  test("Using fixture abstract class") {
    new AnotherTestFixture(Seq(User("Jon Snow"))) {
      db1.getUsers should not be 'empty
      db2.getUsers should not be 'empty
    }
  }

}
