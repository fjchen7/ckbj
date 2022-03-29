package org.ckbj.rpc;

import java.io.IOException;

public class CkbService {
    private JsonRpcClient client;

    public CkbService(String url) {
    }

    public byte[] getBlockHash(byte[] blockHash) throws IOException {
        return CkbJsonRpcRequests.getBlock(blockHash).send(client).getResult();
    }
}