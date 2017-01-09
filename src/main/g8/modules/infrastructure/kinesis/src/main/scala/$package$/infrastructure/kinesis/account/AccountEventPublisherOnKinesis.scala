package $package$.infrastructure.kinesis.account

import $package$.domain.DomainError
import $package$.domain.account.AccountModified

trait AccountEventPublisherOnKinesis {

  def publish(modified: AccountModified): Either[DomainError, AccountModified] = ???
}
