package com.example.infrastructure.dynamodb.account

import com.example.domain.DomainError
import com.example.domain.account.{Account, AccountId}

trait AccountRepositoryOnDynamoDB {

  def save(account: Account): Either[DomainError, Account] = ???

  def get(id: AccountId): Either[DomainError, Option[Account]] = ???

  def findBy(email: String): Either[DomainError, Option[Account]] = ???

  def findAll: Either[DomainError, Seq[Account]] = ???
}
