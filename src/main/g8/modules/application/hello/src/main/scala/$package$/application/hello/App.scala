package $package$.application.hello

import $package$.domain.account.{AccountEventPublisher, AccountRepository}
import $package$.infrastructure.dynamodb.account.AccountRepositoryOnDynamoDB
import $package$.infrastructure.kinesis.account.AccountEventPublisherOnKinesis

class App extends Base {

  override val accountRepository = new AccountRepository with AccountRepositoryOnDynamoDB

  override val accountEventPublisher = new AccountEventPublisher with AccountEventPublisherOnKinesis

}
