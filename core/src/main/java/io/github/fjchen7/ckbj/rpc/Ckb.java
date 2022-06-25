package io.github.fjchen7.ckbj.rpc;

import io.github.fjchen7.ckbj.rpc.type.*;
import io.github.fjchen7.ckbj.type.OutPoint;
import io.github.fjchen7.ckbj.type.Transaction;

import java.util.Arrays;

public class Ckb {
    public static Request<RpcBlock> getBlock(byte[] blockHash) {
        return new Request("get_block", Arrays.asList(blockHash), RpcBlock.class);
    }

    public static Request<RpcBlock> getBlockByNumber(int blockNumber) {
        return new Request("get_block_by_number", Arrays.asList(blockNumber), RpcBlock.class);
    }

    public static Request<RpcHeader> getHeader(byte[] blockHash) {
        return new Request("get_header", Arrays.asList(blockHash), RpcHeader.class);
    }

    public static Request<RpcHeader> getHeaderByNumber(int blockNumber) {
        return new Request("get_header_by_number", Arrays.asList(blockNumber), RpcHeader.class);
    }

    public static Request<RpcOnChainTransaction> getTransaction(byte[] txHash) {
        return new Request("get_transaction", Arrays.asList(txHash), RpcOnChainTransaction.class);
    }

    public static Request<RpcHash> getBlockHash(int blockNumber) {
        return new Request("get_block_hash", Arrays.asList(blockNumber), RpcHash.class);
    }

    public static Request<RpcHeader> getTipHeader() {
        return new Request("get_tip_header", RpcHeader.class);
    }

    public static Request<RpcOnChainCell> getLiveCell(OutPoint outPoint, boolean withData) {
        return new Request("get_live_cell", Arrays.asList(outPoint, withData), RpcOnChainCell.class);
    }

    public static Request<RpcInteger> getTipBlockNumber() {
        return new Request("get_tip_block_number", RpcInteger.class);
    }

    public static Request<RpcHash> sendTransaction(Transaction transaction) {
        return new Request("send_transaction", Arrays.asList(transaction), RpcHash.class);

    }
}
