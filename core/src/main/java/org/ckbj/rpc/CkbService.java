package org.ckbj.rpc;

import java.io.IOException;

public class CkbService {
    private JsonRpcService jsonRpcService;

    public CkbService(JsonRpcService jsonRpcService) {
        this.jsonRpcService = jsonRpcService;
    }

    public CkbService(String url) {
        this(new DefaultHttpService(url));
    }

    public static CkbService testNetService() {
        return new CkbService("https://testnet.ckb.dev");
    }

    public static CkbService mainNetService() {
        return new CkbService("https://mainnet.ckb.dev");
    }

    public byte[] getBlockHash(int blockNumber) throws IOException {
        if (blockNumber < 0) {
            throw new IllegalArgumentException("blockNumber cannot be negative");
        }
        return Ckb.getBlockHash(blockNumber).send(jsonRpcService).getResultOrThrowException();
    }
}