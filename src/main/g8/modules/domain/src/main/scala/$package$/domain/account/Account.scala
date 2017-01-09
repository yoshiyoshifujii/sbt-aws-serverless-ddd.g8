package $package$.domain.account

import $package$.domain.{Entity, ValueObject, Version}

case class AccountId(value: String) extends ValueObject[String]

case class Account(id: AccountId,
                   version: Option[Version],
                   email: String,
                   password: String,
                   name: String) extends Entity[AccountId]

