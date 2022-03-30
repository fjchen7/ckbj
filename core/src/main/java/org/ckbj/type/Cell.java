package org.ckbj.type;

import java.math.BigInteger;

public class Cell {
    private BigInteger capacity = BigInteger.ZERO;
    private Script type;
    private Script lock;

    public BigInteger getCapacity() {
        return capacity;
    }

    public Script getType() {
        return type;
    }

    public Script getLock() {
        return lock;
    }

    public Cell setCapacity(BigInteger capacity) {
        this.capacity = capacity;
        return this;
    }

    public Cell setType(Script type) {
        this.type = type;
        return this;
    }

    public Cell setLock(Script lock) {
        this.lock = lock;
        return this;
    }
}
