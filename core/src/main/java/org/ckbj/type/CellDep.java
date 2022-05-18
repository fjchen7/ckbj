package org.ckbj.type;

import com.google.gson.annotations.SerializedName;

/**
 * Live cell that {@link Transaction} depends on.
 */
public class CellDep {
    private DepType depType;
    private OutPoint outPoint;

    public static Builder builder() {
        return new Builder();
    }

    public DepType getDepType() {
        return depType;
    }

    public void setDepType(DepType depType) {
        this.depType = depType;
    }

    public OutPoint getOutPoint() {
        return outPoint;
    }

    public void setOutPoint(OutPoint outPoint) {
        this.outPoint = outPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellDep cellDep = (CellDep) o;

        if (depType != cellDep.depType) return false;
        return outPoint.equals(cellDep.outPoint);
    }

    @Override
    public int hashCode() {
        int result = depType.hashCode();
        result = 31 * result + outPoint.hashCode();
        return result;
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

    public static final class Builder {
        private DepType depType = DepType.DEP_GROUP;
        private OutPoint outPoint;

        private Builder() {
        }

        public Builder setDepType(DepType depType) {
            this.depType = depType;
            return this;
        }

        public Builder setOutPoint(OutPoint outPoint) {
            this.outPoint = outPoint;
            return this;
        }

        public Builder setOutPoint(byte[] txHash, int index) {
            this.outPoint = new OutPoint(txHash, index);
            return this;
        }

        public Builder setOutPoint(String txHash, int index) {
            this.outPoint = new OutPoint(txHash, index);
            return this;
        }

        public CellDep build() {
            CellDep cellDep = new CellDep();
            cellDep.outPoint = outPoint;
            cellDep.depType = depType;
            return cellDep;
        }
    }
}
