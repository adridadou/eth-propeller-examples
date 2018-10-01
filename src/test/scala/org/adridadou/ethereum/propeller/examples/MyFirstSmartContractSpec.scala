package org.adridadou.ethereum.propeller.examples

import java.io.File

import org.adridadou.ethereum.propeller.solidity.SolidityCompiler
import org.adridadou.ethereum.propeller.values.{EthAddress, SoliditySourceFile}
import org.scalatest.check.Checkers
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * Created by davidroon on 09.04.17.
  * This code is released under Apache 2 license
  */
class MyFirstSmartContractSpec extends FlatSpec with Matchers with Checkers {
  private val solidityCompiler = SolidityCompiler.getInstance()
  private val ethereum = PropellerSetupExamples.scalaWrapperForTest

  private val contractDefinition = solidityCompiler.compileSrc(SoliditySourceFile.from(new File("src/test/resources/myFirstContract.sol"))).findContract("myFirstContract").orElseThrow()

  private def createContract():EthAddress = {
    Await.result(ethereum.publishContract(contractDefinition, PropellerSetupExamples.testAccount), Duration.Inf)
  }

  "My first smart contract" should "be able to talk to the contract" in {
    val contractAddress = createContract()
    val contract = ethereum.createContractProxy[MyFirstContract](contractDefinition, contractAddress, PropellerSetupExamples.testAccount)
    contract.getString shouldBe "hello world"
  }

  it should "be able to set and get a value" in {
    val contractAddress = createContract()
    val contract = ethereum.createContractProxy[MyFirstContract](contractDefinition, contractAddress, PropellerSetupExamples.testAccount)
    contract.getNumber shouldBe 0
    Await.result(contract.setNumber(100), Duration.Inf)
    contract.getNumber shouldBe 100
  }
}

trait MyFirstContract {
  def getString:String
  def getNumber:Int
  def setNumber(i:Int):Future[Void]
}