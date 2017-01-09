package $package$.domain.account

import $package$.domain.DomainError

case class AccountModified(accountId: AccountId,
                           email: String,
                           name: String)

trait AccountEventPublisher {

  def publish(modified: AccountModified): Either[DomainError, AccountModified]

}
