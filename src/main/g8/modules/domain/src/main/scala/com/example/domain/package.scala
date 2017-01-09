package com.example

import java.util.UUID

package object domain {

  type Version = Long

  trait Entity[ID] {

    val id: ID
    val version: Option[Version]

    override def equals(obj: Any): Boolean =
      obj match {
        case that: Entity[_] => id == that.id
        case _ => false
      }

    override def hashCode: Int = 31 * id.##
  }

  trait ValueObject[V] {

    val value: V

    override def equals(obj: Any): Boolean =
      obj match {
        case that: ValueObject[_] => value == that.value
        case _ => false
      }

    override def hashCode: Int = 31 * value.##
  }

  trait Id[ID <: ValueObject[_]] {

    protected def generateUUId: String =
      UUID.randomUUID().toString.split("-").mkString("").toUpperCase

    def generate: ID
  }

  trait DomainError

  case class SystemError(cause: Throwable) extends DomainError {
    override def toString = cause.getMessage
  }
  case class ApplicationError(message: String) extends DomainError {
    override def toString = message
  }
}
