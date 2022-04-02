package com.ckbj.chain;

import org.ckbj.chain.Contract;
import org.ckbj.chain.Network;
import org.ckbj.chain.NetworkDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NetworkDetailTest {
    @Test
    public void testDefaultInstance() {
        NetworkDetail lina = NetworkDetail.defaultInstance(Network.LINA);
        NetworkDetail aggron = NetworkDetail.defaultInstance(Network.AGGRON);
        for (Contract.Standard contractName: Contract.Standard.values()) {
            Assertions.assertNotNull(lina.get(contractName));
            Assertions.assertNotNull(aggron.get(contractName));
        }
    }
}
