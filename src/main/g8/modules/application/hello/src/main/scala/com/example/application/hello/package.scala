package com.example.application

import com.example.domain.account.AccountId

package object hello {

  case class StageVariables(env: String,
                            region: String)

  case class Authorizer(accountId: AccountId)

  case class Request(stageVariables: StageVariables,
                     authorizer: Authorizer)
}
