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
        byte[] hash = Hex.toByteArray("0x82001535467ebf5dc0f2b4b7236c9216be7c573908f67850eeb2b602cfc6213e");
        Transaction transaction = CkbService.defaultInstance(Network.AGGRON)
                .getTransaction(hash)
                .getTransaction();
        Assertions.assertArrayEquals(hash, transaction.hash());
    }
}