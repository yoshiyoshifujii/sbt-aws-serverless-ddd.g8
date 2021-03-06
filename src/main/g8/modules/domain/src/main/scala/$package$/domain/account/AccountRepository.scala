package $package$.domain.account

import $package$.domain.DomainError

trait AccountRepository {

  def save(account: Account): Either[DomainError, Account]

  def get(id: AccountId): Either[DomainError, Option[Account]]

  def findBy(email: String): Either[DomainError, Option[Account]]

  def findAll: Either[DomainError, Seq[Account]]

}
