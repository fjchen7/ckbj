package org.ckbj.type;

public final class OutPoint {
    private byte[] txHash;
    private int index;

    private OutPoint() {
    }

    public static Builder build() {
        return new Builder();
    }

    public byte[] getTxHash() {
        return txHash;
    }

    public int getIndex() {
        return index;
    }

    public static final class Builder {
        private byte[] txHash;
        private int index;

        private Builder() {
        }

        public Builder setTxHash(byte[] txHash) {
            if (txHash.length != 32) {
                throw new IllegalArgumentException("txHash should be 32 bytes");
            }
            this.txHash = txHash;
            return this;
        }

        public Builder setIndex(int index) {
            this.index = index;
            return this;
        }

        public OutPoint build() {
            OutPoint outPoint = new OutPoint();
            outPoint.txHash = this.txHash;
            outPoint.index = this.index;
            return outPoint;
        }
    }
}
