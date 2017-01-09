package $package$.infrastructure.dynamodb.account

import $package$.domain.DomainError
import $package$.domain.account.{Account, AccountId}

trait AccountRepositoryOnDynamoDB {

  def save(account: Account): Either[DomainError, Account] = ???

  def get(id: AccountId): Either[DomainError, Option[Account]] = ???

  def findBy(email: String): Either[DomainError, Option[Account]] = ???

  def findAll: Either[DomainError, Seq[Account]] = ???
}
