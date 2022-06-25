package io.github.fjchen7.ckbj.type;

import io.github.fjchen7.ckbj.CkbService;
import io.github.fjchen7.ckbj.chain.Network;
import io.github.fjchen7.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HeaderTest {
    @Test
    public void testHash() throws IOException {
        Header header = CkbService.getInstance(Network.TESTNET).getHeader(1024);
        Assertions.assertArrayEquals(
                Hex.toByteArray("0x741813929fdb2bc59713995b0e402997dfe5f51f94193940fc6866c63a89d27e"),
                header.hash());
    }
}