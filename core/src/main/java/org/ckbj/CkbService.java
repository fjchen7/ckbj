package org.ckbj;

import org.ckbj.chain.Network;
import org.ckbj.rpc.Ckb;
import org.ckbj.rpc.service.DefaultHttpService;
import org.ckbj.rpc.service.JsonRpcService;
import org.ckbj.type.*;
import org.ckbj.utils.Hex;

import java.io.IOException;

public class CkbService {
    private JsonRpcService jsonRpcService;
    private static CkbService LINA_SERVICE;
    private static CkbService AGGRON_SERVICE;

    public CkbService(JsonRpcService jsonRpcService) {
        this.jsonRpcService = jsonRpcService;
    }

    public CkbService(String url) {
        this(new DefaultHttpService(url));
    }

    public static CkbService defaultInstance(Network network) {
        switch (network) {
            case LINA:
                if (LINA_SERVICE == null) {
                    LINA_SERVICE = new CkbService("https://mainnet.ckb.dev");
                }
                return LINA_SERVICE;
            case AGGRON:
                if (AGGRON_SERVICE == null) {
                    AGGRON_SERVICE = new CkbService("https://testnet.ckb.dev");
                }
                return AGGRON_SERVICE;
            default:
                throw new UnsupportedOperationException("Unknown network");
        }
    }

    public Block getBlock(byte[] blockHash) throws IOException {
        return Ckb.getBlock(blockHash).send(jsonRpcService).getResultOrThrowException();
    }

    public Block getBlock(String blockHash) throws IOException {
        return getBlock(Hex.toByteArray(blockHash));
    }

    public Block getBlock(int blockNumber) throws IOException {
        return Ckb.getBlockByNumber(blockNumber).send(jsonRpcService).getResultOrThrowException();
    }

    public Header getHeader(byte[] blockHash) throws IOException {
        return Ckb.getHeader(blockHash).send(jsonRpcService).getResultOrThrowException();
    }

    public Header getHeader(String blockHash) throws IOException {
        return getHeader(Hex.toByteArray(blockHash));
    }

    public Header getHeader(int blockNumber) throws IOException {
        return Ckb.getHeaderByNumber(blockNumber).send(jsonRpcService).getResultOrThrowException();
    }

    public OnChainTransaction getTransaction(byte[] txHash) throws IOException {
        return Ckb.getTransaction(txHash).send(jsonRpcService).getResultOrThrowException();
    }

    public OnChainTransaction getTransaction(String txHash) throws IOException {
        return getTransaction(Hex.toByteArray(txHash));
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

    public OnChainCell getLiveCell(OutPoint outPoint, boolean withData) throws IOException {
        OnChainCell cell = Ckb.getLiveCell(outPoint, withData).send(jsonRpcService).getResultOrThrowException();
        cell.setOutPoint(outPoint);
        return cell;
    }
}