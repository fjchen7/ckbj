package org.ckbj.type;

import com.google.gson.annotations.SerializedName;

/**
 * Live cell that {@link Transaction} depends on.
 */
public class CellDep {
    private OutPoint outPoint;
    private DepType depType;

    private CellDep() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public OutPoint getOutPoint() {
        return outPoint;
    }

    public DepType getDepType() {
        return depType;
    }

    public static final class Builder {
        private OutPoint outPoint;
        private DepType depType;

        private Builder() {
            setDepType(DepType.DEP_GROUP);
        }

        public Builder setOutPoint(byte[] txHash, int index) {
            OutPoint outPoint = OutPoint.build()
                    .setTxHash(txHash)
                    .setIndex(index)
                    .build();
            this.outPoint = outPoint;
            return this;
        }

        public Builder setOutPoint(OutPoint outPoint) {
            this.outPoint = outPoint;
            return this;
        }

        public Builder setDepType(DepType depType) {
            this.depType = depType;
            return this;
        }

        public CellDep build() {
            CellDep cellDep = new CellDep();
            cellDep.outPoint = this.outPoint;
            cellDep.depType = this.depType;
            return cellDep;
        }
    }

    public enum DepType {
        @SerializedName("code")
        CODE(0x00),
        @SerializedName("dep_group")
        DEP_GROUP(0x01);

        private byte value;

        DepType(int value) {
            this.value = (byte) value;
        }

        public byte toByte() {
            return value;
        }
    }
}
