package org.ckbj;

import org.ckbj.chain.Network;
import org.ckbj.rpc.JsonRpcException;
import org.ckbj.type.*;
import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CkbServiceTest {
    private CkbService service = CkbService.defaultInstance(Network.AGGRON);

    @Test
    public void testGetBlockHash() throws IOException {
        byte[] hash = service.getBlockHash(1024);
        Assertions.assertEquals("0x741813929fdb2bc59713995b0e402997dfe5f51f94193940fc6866c63a89d27e", Hex.encode(hash));
    }

    @Test
    public void testGetTransaction() throws IOException {
        DetailedTransaction transaction = service.getTransaction("0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1");
        Assertions.assertEquals(4, transaction.getCellDeps().size());
        Assertions.assertEquals(1, transaction.getInputs().size());
        Assertions.assertEquals(3, transaction.getOutputs().size());
        Assertions.assertEquals(new BigInteger("30000000000"),
                transaction.getOutputs().get(0).getCapacity());
        Assertions.assertEquals(new BigInteger("11800000000"),
                transaction.getOutputs().get(1).getCapacity());
        Assertions.assertEquals(new BigInteger("19994640399880000"),
                transaction.getOutputs().get(2).getCapacity());
        Assertions.assertEquals(3, transaction.getOutputsData().size());
        Assertions.assertEquals(2, transaction.getWitnesses().size());
        Assertions.assertEquals(DetailedTransaction.Status.COMMITTED, transaction.getStatus());
        Assertions.assertArrayEquals(Hex.decode("0xb9d83075b7694d624d46821848e1286296b9d0ab724a2a5803af9639bd071a6b"),
                transaction.getBlockHash());
    }

    @Test
    public void testGetBlock() throws IOException {
        Block block = service.getBlock("0xb9d83075b7694d624d46821848e1286296b9d0ab724a2a5803af9639bd071a6b");
        Assertions.assertEquals(2, block.getTransactions().size());
        Assertions.assertNotNull(block.getHeader());
        Assertions.assertEquals(4930, block.getHeader().getNumber());

        block = service.getBlock(4930);
        Assertions.assertEquals(4930, block.getHeader().getNumber());
    }

    @Test
    public void testGetTipBlockNumber() throws IOException {
        int blockNumber = service.getTipHeaderBlockNumber();
        Assertions.assertNotEquals(0, blockNumber);
    }

    @Test
    public void testGetHeader() throws IOException {
        Header header = service.getHeader("0x741813929fdb2bc59713995b0e402997dfe5f51f94193940fc6866c63a89d27e");
        Assertions.assertEquals(1024, header.getNumber());
        assertEquals(500, header.getEpoch().getLength());
        assertEquals(24, header.getEpoch().getBlockIndex());
        assertEquals(1, header.getEpoch().getNumber());
        assertEquals(1590143167052L, header.getTimestamp());

        header = service.getHeader(1024);
        Assertions.assertEquals(1024, header.getNumber());

        header = service.getTipHeader();
        Assertions.assertNotNull(header);
        Assertions.assertNotEquals(0, header.getNumber());
    }

    @Test
    public void testGetLiveCell() throws IOException {
        OutPoint outPoint = new OutPoint("0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1", 0);
        DetailedCell detailedCell = service.getLiveCell(outPoint, true);
        Assertions.assertEquals(new BigInteger("30000000000"), detailedCell.getCapacity());
        Assertions.assertArrayEquals(Hex.decode("0xa999bfb3735fdff4f26016b47712bb64ffbe88e62deec0e4e0d69ea8d54012778877ddfcc3ec76d5a59630a162828761147dd36052ca0db8d024ab68591e4826"),
                detailedCell.getLock().getArgs());
        Assertions.assertEquals(Script.HashType.DATA, detailedCell.getLock().getHashType());
        Assertions.assertArrayEquals(Hex.decode("0x82d76d1b75fe2fd9a27dfbaa65a039221a380d76c926f378d3f81cf3e7e13f2e"), detailedCell.getType().getCodeHash());
        Assertions.assertEquals(Script.HashType.TYPE, detailedCell.getType().getHashType());
        Assertions.assertArrayEquals(new byte[]{}, detailedCell.getType().getArgs());
        Assertions.assertArrayEquals(Hex.decode("0x0000000000000000"), detailedCell.getData());
    }

    @Test
    public void testException() {
        Assertions.assertThrows(JsonRpcException.class, () -> {
            service.getBlock("0x");
        });
    }
}
