package org.ckbj.type;

import com.google.gson.annotations.SerializedName;

/**
 * Live cell that {@link Transaction} depends on.
 */
public class CellDep {
    private OutPoint outPoint;
    private DepType depType = DepType.DEP_GROUP;

    public OutPoint getOutPoint() {
        return outPoint;
    }

    public DepType getDepType() {
        return depType;
    }

    public CellDep setOutPoint(byte[] txHash, int index) {
        OutPoint outPoint = new OutPoint(txHash, index);
        this.outPoint = outPoint;
        return this;
    }

    public CellDep setOutPoint(OutPoint outPoint) {
        this.outPoint = outPoint;
        return this;
    }

    public CellDep setDepType(DepType depType) {
        this.depType = depType;
        return this;
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
