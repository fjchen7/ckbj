package org.ckbj.type;

import org.ckbj.utils.Hex;

import java.util.Arrays;

public final class OutPoint {
    private byte[] txHash;
    private int index;

    public OutPoint(byte[] txHash, int index) {
        setTxHash(txHash);
        setIndex(index);
    }

    public OutPoint(String txHash, int index) {
        setTxHash(txHash);
        setIndex(index);
    }

    public byte[] getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        setTxHash(Hex.toByteArray(txHash));
    }

    public void setTxHash(byte[] txHash) {
        if (txHash.length != 32) {
            throw new IllegalArgumentException("txHash should be 32 bytes");
        }
        this.txHash = txHash;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutPoint outPoint = (OutPoint) o;

        if (index != outPoint.index) return false;
        return Arrays.equals(txHash, outPoint.txHash);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(txHash);
        result = 31 * result + index;
        return result;
    }
}
