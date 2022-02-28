package org.nervos.protocol.type;

import java.math.BigInteger;

public class DetailedCell {
    private BigInteger capacity;
    private Script typeScript;
    private Script lockScript;
    private byte[][] data;
    private OutPoint outPoint;

    public BigInteger getCapacity() {
        return capacity;
    }

    public Script getTypeScript() {
        return typeScript;
    }

    public Script getLockScript() {
        return lockScript;
    }

    public byte[][] getData() {
        return data;
    }

    public OutPoint getOutPoint() {
        return outPoint;
    }
}
