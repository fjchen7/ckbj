package org.ckbj.type;

import org.ckbj.CkbService;
import org.ckbj.chain.Network;
import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class DetailedCellTest {

    @Test
    public void testHash() throws IOException {
        OutPoint outPoint = new OutPoint("0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1", 0);
        DetailedCell cell = CkbService.defaultInstance(Network.AGGRON).getLiveCell(outPoint, true);
        Assertions.assertArrayEquals(
                Hex.toByteArray("0xa13611ce422f5f343037424a8ae928d60285dbe02f105b6f71882c2d233f3688"),
                cell.dataHash());
    }
}