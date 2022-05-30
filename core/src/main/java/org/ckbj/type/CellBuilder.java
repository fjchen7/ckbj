package org.ckbj.type;

import org.ckbj.chain.address.Address;
import org.ckbj.utils.Capacity;
import org.ckbj.utils.Hex;

public final class CellBuilder {
    private long capacity;
    private Script type;
    private Script lock;
    private byte[] data = new byte[0];

    CellBuilder() {
    }

    public CellBuilder setCapacityInShannon(long capacityInShannon) {
        if (capacityInShannon < 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacityInShannon;
        return this;
    }

    public CellBuilder setCapacityInBytes(double capacityInBytes) {
        if (capacityInBytes < 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = Capacity.bytesToShannon(capacityInBytes);
        return this;
    }

    public CellBuilder setType(Script type) {
        this.type = type;
        return this;
    }

    public CellBuilder setLock(Script lock) {
        this.lock = lock;
        return this;
    }

    public CellBuilder setLock(Address address) {
        this.lock = address.getScript();
        return this;
    }

    public CellBuilder setLock(String address) {
        return setLock(Address.decode(address));
    }

    public CellBuilder setData(byte[] data) {
        this.data = data;
        return this;
    }

    public CellBuilder setData(String data) {
        this.data = Hex.toByteArray(data);
        return this;
    }

    public Cell build() {
        Cell cell = new Cell();
        cell.setCapacity(capacity);
        cell.setType(type);
        cell.setLock(lock);
        cell.setData(data);
        return cell;
    }
}
