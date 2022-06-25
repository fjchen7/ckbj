package io.github.fjchen7.ckbj.type;

import io.github.fjchen7.ckbj.CkbService;
import io.github.fjchen7.ckbj.chain.Network;
import io.github.fjchen7.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class OnChainCellTest {

    @Test
    public void testHash() throws IOException {
        OutPoint outPoint = new OutPoint("0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1", 0);
        OnChainCell cell = CkbService.getInstance(Network.TESTNET).getLiveCell(outPoint, true);
        Assertions.assertArrayEquals(
                Hex.toByteArray("0xa13611ce422f5f343037424a8ae928d60285dbe02f105b6f71882c2d233f3688"),
                cell.dataHash());
    }
}