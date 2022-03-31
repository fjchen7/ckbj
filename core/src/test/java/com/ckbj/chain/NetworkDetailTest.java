package com.ckbj.chain;

import org.ckbj.chain.ContractName;
import org.ckbj.chain.Network;
import org.ckbj.chain.NetworkDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class NetworkDetailTest {
    @Test
    public void loadContracts() throws IOException {
        NetworkDetail lina = NetworkDetail.getInstance(Network.LINA);
        NetworkDetail aggron = NetworkDetail.getInstance(Network.AGGRON);
        for (ContractName contractName: ContractName.values()) {
            Assertions.assertNotNull(lina.get(contractName));
            Assertions.assertNotNull(aggron.get(contractName));
        }

    }
}
