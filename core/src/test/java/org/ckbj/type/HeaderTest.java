package org.ckbj.type;

import org.ckbj.CkbService;
import org.ckbj.chain.Network;
import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HeaderTest {
    @Test
    public void testHash() throws IOException {
        Header header = CkbService.defaultInstance(Network.TESTNET).getHeader(1024);
        Assertions.assertArrayEquals(
                Hex.toByteArray("0x741813929fdb2bc59713995b0e402997dfe5f51f94193940fc6866c63a89d27e"),
                header.hash());
    }
}