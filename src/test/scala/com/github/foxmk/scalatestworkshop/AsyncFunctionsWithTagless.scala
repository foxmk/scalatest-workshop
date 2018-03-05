package com.github.foxmk.scalatestworkshop

//import scala.concurrent.Future
//import cats.{Id, Monad}
//import cats.syntax.all._
//
//case class Book(id: String, name: String)
//
//trait BookRepository[F[_]] {
//  def getBook(id: String): F[Option[Book]]
//}
//
//class BookRepositoryImpl[F[_]: Monad] extends BookRepository[F] {
//  def getBook(id: String): Future[Option[Book]] = id match {
//    case "valid"        => Future.successful(Some(Book(id = "valid", name = "A Perfectly Valid Book")))
//    case "non_existing" => Future.successful(None)
//    case _              => Future.failed(new Exception("Invalid book"))
//  }
//}
//
//class AsyncFunctionsWithTagless {}
