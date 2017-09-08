package org.adridadou.ethereum.propeller.examples

import java.io.File
import java.util
import java.util.Date
import java.util.concurrent.CompletableFuture

import org.adridadou.ethereum.propeller.solidity.SolidityContractDetails
import org.adridadou.ethereum.propeller.values._
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

  "Ethereum Contract" should "be able to observe an event from the ethereum network" in {

    val ethereum = PropellerSetupExamples.scalaWrapperForTest
    val compilationResult = ethereum.compile(SoliditySourceFile.from(new File("src/test/resources/contractEvents.sol")))
    val myCompiledContract:SolidityContractDetails = compilationResult.findContract("contractEvents").get
    val eventDefinition = ethereum.findEventDefinition[MyEvent](myCompiledContract, "MyEvent").get

    val futureAddress = ethereum.publishContract(myCompiledContract, PropellerSetupExamples.testAccount)
    val address:EthAddress = Await.result(futureAddress, Duration.Inf)
    val myContract = ethereum.createContractProxy[ContractEvents](myCompiledContract, address,PropellerSetupExamples.testAccount)
    val observeEventWithInfo = ethereum.observeEventsWIthInfo[MyEvent](eventDefinition, address)

    myContract.createEvent("my event is here and it is much longer than anticipated")
    observeEventWithInfo.foreach(result => {
      result.getTransactionHash shouldBe EthHash.of("6717c8616d06184e589aae321d1a2349679675fc6c7af95368c2e5f3e71daaef")
      result.getResult.value shouldBe "my event is here and it is much longer than anticipated"
    })
  }
}


trait MyContract2 {
  def myMethod(value: String): EthCall[Integer]

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

trait ContractEvents {
  def createEvent(value: String): CompletableFuture[Void]
}

case class MyReturnType(val1: Boolean, val2: String, val3: Integer)

case class MyEvent(from: EthAddress, to: EthAddress, value: String, ethData: EthData)
