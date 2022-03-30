package org.ckbj.rpc;

import org.ckbj.rpc.type.RpcBlockHash;

import java.util.Arrays;

public class Ckb {
    public static Request<RpcBlockHash> getBlockHash(int blockNumber) {
        return new Request("get_block_hash", Arrays.asList(blockNumber), RpcBlockHash.class);
    }
}
