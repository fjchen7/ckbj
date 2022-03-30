package org.ckbj.rpc;

import org.ckbj.type.*;
import org.ckbj.utils.Hex;

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

    public Block getBlock(byte[] blockHash) throws IOException {
        return Ckb.getBlock(blockHash).send(jsonRpcService).getResultOrThrowException();
    }

    public Block getBlock(String blockHash) throws IOException {
        return getBlock(Hex.decode(blockHash));
    }

    public Block getBlock(int blockNumber) throws IOException {
        return Ckb.getBlockByNumber(blockNumber).send(jsonRpcService).getResultOrThrowException();
    }

    public Header getHeader(byte[] blockHash) throws IOException {
        return Ckb.getHeader(blockHash).send(jsonRpcService).getResultOrThrowException();
    }

    public Header getHeader(String blockHash) throws IOException {
        return getHeader(Hex.decode(blockHash));
    }

    public Header getHeader(int blockNumber) throws IOException {
        return Ckb.getHeaderByNumber(blockNumber).send(jsonRpcService).getResultOrThrowException();
    }

    public DetailedTransaction getTransaction(byte[] txHash) throws IOException {
        return Ckb.getTransaction(txHash).send(jsonRpcService).getResultOrThrowException();
    }

    public DetailedTransaction getTransaction(String txHash) throws IOException {
        return getTransaction(Hex.decode(txHash));
    }

    public byte[] getBlockHash(int blockNumber) throws IOException {
        if (blockNumber < 0) {
            throw new IllegalArgumentException("blockNumber cannot be negative");
        }
        return Ckb.getBlockHash(blockNumber).send(jsonRpcService).getResultOrThrowException();
    }

    public Header getTipHeader() throws IOException {
        return Ckb.getTipHeader().send(jsonRpcService).getResultOrThrowException();
    }

    public int getTipHeaderBlockNumber() throws IOException {
        return Ckb.getTipBlockNumber().send(jsonRpcService).getResultOrThrowException();
    }

    public DetailedCell getLiveCell(OutPoint outPoint, boolean withData) throws IOException {
        return Ckb.getLiveCell(outPoint, withData).send(jsonRpcService).getResultOrThrowException();
    }
}