package com.example.infrastructure.kinesis.account

import com.example.domain.DomainError
import com.example.domain.account.AccountModified

trait AccountEventPublisherOnKinesis {

  def publish(modified: AccountModified): Either[DomainError, AccountModified] = ???
}
