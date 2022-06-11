package org.ckbj.chain;

import org.ckbj.type.Script;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NetworkTest {

    @Test
    public void testGetContract() {
        for (Contract.Name contractName: Contract.Name.values()) {
            Assertions.assertNotNull(Network.MAINNET.getContract(contractName));
            Assertions.assertNotNull(Network.TESTNET.getContract(contractName));
        }

        Assertions.assertNull(Network.MAINNET.getContract((Contract.Name) null));
        Assertions.assertNull(Network.MAINNET.getContract((Script) null));
    }

    @Test
    public void testGetContractName() {
        Network network = Network.MAINNET;
        Script script = Script.builder()
                .setCodeHash("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
                .setArgs("0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b")
                .setHashType(Script.HashType.TYPE)
                .build();
        Assertions.assertEquals(
                Contract.Name.SECP256K1_BLAKE160_SIGHASH_ALL,
                network.getContractName(script));

        script = Script.builder()
                .setCodeHash("0x1bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
                .build();
        Assertions.assertNull(network.getContractName(script));
        Assertions.assertNull(network.getContractName(null));
    }
}