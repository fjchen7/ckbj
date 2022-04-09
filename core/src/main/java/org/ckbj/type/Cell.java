package org.ckbj.type;

import org.ckbj.crypto.Blake2b;
import org.ckbj.utils.Hex;

import java.math.BigInteger;

public class Cell {
    private BigInteger capacity = BigInteger.ZERO;
    private Script type;
    private Script lock;
    private byte[] data = new byte[0];

    public Cell() {}

    public Cell(Cell cell) {
        this.capacity = cell.capacity;
        this.type = cell.type;
        this.lock = cell.lock;
        this.data = cell.data;
    }

    public BigInteger getCapacity() {
        return capacity;
    }

    public Cell setCapacity(BigInteger capacity) {
        this.capacity = capacity;
        return this;
    }

    public Script getType() {
        return type;
    }

    public Cell setType(Script type) {
        this.type = type;
        return this;
    }

    public Script getLock() {
        return lock;
    }

    public Cell setLock(Script lock) {
        this.lock = lock;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public Cell setData(byte[] data) {
        this.data = data;
        return this;
    }

    public Cell setData(String data) {
        return setData(Hex.toByteArray(data));
    }

    public byte[] dataHash() {
        return Blake2b.digest256(data);
    }
}
