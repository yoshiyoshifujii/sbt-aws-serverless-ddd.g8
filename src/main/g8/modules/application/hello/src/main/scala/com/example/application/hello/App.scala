package com.example.application.hello

import com.example.domain.account.{AccountEventPublisher, AccountRepository}
import com.example.infrastructure.dynamodb.account.AccountRepositoryOnDynamoDB
import com.example.infrastructure.kinesis.account.AccountEventPublisherOnKinesis

class App extends Base {

  override val accountRepository = new AccountRepository with AccountRepositoryOnDynamoDB

  override val accountEventPublisher = new AccountEventPublisher with AccountEventPublisherOnKinesis

}
