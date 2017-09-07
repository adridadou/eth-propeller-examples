package org.adridadou.ethereum.propeller.examples

import java.io.File
import java.util
import java.util.Date
import java.util.concurrent.CompletableFuture

import org.adridadou.ethereum.propeller.solidity.SolidityContractDetails
import org.adridadou.ethereum.propeller.values.{EthAccount, EthAddress, Payable, SoliditySourceFile}
import org.scalatest.check.Checkers
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration


/**
  * Created by davidroon on 09.04.17.
  * This code is released under Apache 2 license
  */
class CompileAndDeployExample extends FlatSpec with Matchers with Checkers {

  "Ethereum Contract" should "be deployable in a test environment and get an address in return and then create a proxy object of the contract" in {
    val ethereum = PropellerSetupExamples.scalaWrapperForTest
    val compilationResult = ethereum.compile(SoliditySourceFile.from(new File("src/test/resources/contract.sol")))
    val myCompiledContract:SolidityContractDetails = compilationResult.findContract("myContract2").get

    val futureAddress = ethereum.publishContract(myCompiledContract, PropellerSetupExamples.testAccount)
    val address:EthAddress = Await.result(futureAddress, Duration.Inf)

    val contractInterface = ethereum.createContractProxy[MyContract2](myCompiledContract, address, PropellerSetupExamples.testAccount)

    contractInterface.getM shouldBe MyReturnType(true,"hello", 34)
  }
}


trait MyContract2 {
  def myMethod(value: String): Future[Integer]

  def myMethod2(value: String): Future[Void]

  def myMethod3(value: String): Payable[Void]

  def getI1: String

  def getI2: String

  def getT: Boolean

  def getM: MyReturnType

  def getArray: util.List[Integer]

  def getSet: util.Set[Integer]

  def throwMe: CompletableFuture[Void]

  def getOwner: EthAddress

  def getInitTime(date: Date): Date

  def getAccountAddress(account: EthAccount): EthAddress
}


case class MyReturnType(val1: Boolean, val2: String, val3: Integer)
