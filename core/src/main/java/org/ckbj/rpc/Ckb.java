package org.ckbj.rpc;

import org.ckbj.rpc.type.*;
import org.ckbj.type.OutPoint;

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

    public static Request<RpcDetailedTransaction> getTransaction(byte[] txHash) {
        return new Request("get_transaction", Arrays.asList(txHash), RpcDetailedTransaction.class);
    }

    public static Request<RpcBlockHash> getBlockHash(int blockNumber) {
        return new Request("get_block_hash", Arrays.asList(blockNumber), RpcBlockHash.class);
    }

    public static Request<RpcHeader> getTipHeader() {
        return new Request("get_tip_header", RpcHeader.class);
    }

    public static Request<RpcDetailedCell> getLiveCell(OutPoint outPoint, boolean withData) {
        return new Request("get_live_cell", Arrays.asList(outPoint, withData), RpcDetailedCell.class);
    }

    public static Request<RpcBlockNumber> getTipBlockNumber() {
        return new Request("get_tip_block_number", RpcBlockNumber.class);
    }
}
