package com.ckbj.chain;

import org.ckbj.chain.ContractName;
import org.ckbj.chain.Network;
import org.ckbj.chain.NetworkDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class NetworkDetailTest {
    @Test
    public void loadContracts() {
        NetworkDetail lina = NetworkDetail.defaultInstance(Network.LINA);
        NetworkDetail aggron = NetworkDetail.defaultInstance(Network.AGGRON);
        for (ContractName contractName: ContractName.values()) {
            Assertions.assertNotNull(lina.get(contractName));
            Assertions.assertNotNull(aggron.get(contractName));
        }
    }
}
