package io.github.fjchen7.ckbj.type;

import io.github.fjchen7.ckbj.crypto.Blake2b;
import io.github.fjchen7.ckbj.utils.Capacity;
import io.github.fjchen7.ckbj.utils.Hex;

public class Cell {
    private long capacity;
    private Script type;
    private Script lock;
    private byte[] data = new byte[0];

    public static CellBuilder builder() {
        return new CellBuilder();
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
    }

    public Script getType() {
        return type;
    }

    public void setType(Script type) {
        this.type = type;
    }

    public Script getLock() {
        return lock;
    }

    public void setLock(Script lock) {
        this.lock = lock;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setData(String data) {
        setData(Hex.toByteArray(data));
    }

    public byte[] dataHash() {
        return Blake2b.digest(data);
    }

    public long getOccupiedCapacity(boolean includeData) {
        return Capacity.occupation(this, includeData);
    }

    public boolean isCapacityEnough() {
        return capacity >= getOccupiedCapacity(true);
    }

}
