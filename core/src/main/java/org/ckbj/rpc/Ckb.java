package org.ckbj.rpc;

import org.ckbj.rpc.type.RpcBlockHash;

import java.util.Arrays;

public class Ckb {
    public static Request<RpcBlockHash> getBlock(byte[] blockHash) {
        return new Request("get_block", Arrays.asList(blockHash), RpcBlockHash.class);
    }
//    public ExampleC getE() {
//        return null;
//    }
}
