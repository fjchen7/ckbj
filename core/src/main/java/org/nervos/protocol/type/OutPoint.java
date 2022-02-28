package org.nervos.protocol.type;

/**
 * Pointer to a live cell on CKB chain.
 */
public class OutPoint {
    private byte[] TxHash;
    private int index;

    public byte[] getTxHash() {
        return TxHash;
    }

    public void setTxHash(byte[] txHash) {
        TxHash = txHash;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
