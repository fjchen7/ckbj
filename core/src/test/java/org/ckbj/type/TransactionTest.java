package org.ckbj.type;

import org.ckbj.CkbService;
import org.ckbj.chain.Network;
import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TransactionTest {
    @Test
    public void testHash() throws IOException {
        byte[] hash = Hex.toByteArray("0xffcd8f05518e63937fc59ddf1801fc78da96fdfebd1e0a65f677d92795474e3e");
        Transaction transaction = CkbService.defaultInstance(Network.TESTNET)
                .getTransaction(hash)
                .getTransaction();
        Assertions.assertArrayEquals(hash, transaction.hash());
    }
}