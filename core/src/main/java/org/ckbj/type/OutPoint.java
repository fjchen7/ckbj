package org.ckbj.type;

import org.ckbj.utils.Hash;

public final class OutPoint {
    private byte[] txHash;
    private int index;

    public OutPoint(byte[] txHash, int index) {
        setTxHash(txHash);
        setIndex(index);
    }

    public byte[] getTxHash() {
        return txHash;
    }

    public int getIndex() {
        return index;
    }

    public OutPoint setTxHash(byte[] txHash) {
        if (!Hash.isHash(txHash)) {
            throw new IllegalArgumentException("txHash should be 32 bytes");
        }
        this.txHash = txHash;
        return this;
    }

    public OutPoint setIndex(int index) {
        this.index = index;
        return this;
    }
}
