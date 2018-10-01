package org.adridadou.ethereum.propeller.examples

import org.adridadou.ethereum.EthjEthereumFacadeProvider
import org.adridadou.ethereum.ethj.{EthereumJConfigs, TestConfig}
import org.adridadou.ethereum.propeller.{EthereumFacade, RpcEthereumFacadeProvider}
import org.adridadou.ethereum.propeller.keystore.AccountProvider
import org.adridadou.ethereum.propeller.values.config.InfuraKey
import org.adridadou.ethereum.propeller.values.{EthAccount, EthValue, GasPrice}
import org.adridadou.propeller.scala.ScalaEthereumFacade

/**
  * Created by davidroon on 09.04.17.
  * This code is released under Apache 2 license
  */
object PropellerSetupExamples {
  val testAccount: EthAccount = AccountProvider.fromSeed("seed string")


  def configureForTest():EthereumFacade = EthjEthereumFacadeProvider
    .forTest(TestConfig.builder()
        .balance(testAccount, EthValue.ether(1.234))
        .gasLimit(1222220222)
        .gasPrice(new GasPrice(EthValue.wei(10000000)))
      .build())

  def configureForEmbeddedNode():EthereumFacade = EthjEthereumFacadeProvider
        .forNetwork(EthereumJConfigs.ropsten()).create()

  def configureRemoteNode():EthereumFacade = RpcEthereumFacadeProvider
    .forRemoteNode("http://localhost:5001",RpcEthereumFacadeProvider.ROPSTEN_CHAIN_ID)

  def configureInfura():EthereumFacade = RpcEthereumFacadeProvider
    .forInfura(InfuraKey.of("my infura key")).createRopsten()

  def scalaWrapperForTest = ScalaEthereumFacade(configureForTest())
}
