package org.ckbj.type;

import org.ckbj.crypto.Blake2b;
import org.ckbj.utils.Hex;

import java.math.BigInteger;

public class Cell {
    private BigInteger capacity = BigInteger.ZERO;
    private Script type;
    private Script lock;
    private byte[] data = new byte[0];

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Cell cell) {
        return new Builder(cell);
    }

    public BigInteger getCapacity() {
        return capacity;
    }

    public void setCapacity(BigInteger capacity) {
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

    public static final class Builder {
        private BigInteger capacity = BigInteger.ZERO;
        private Script type;
        private Script lock;
        private byte[] data = new byte[0];

        private Builder() {
        }

        public Builder(Cell cell) {
            this.capacity = cell.capacity;
            this.type = cell.type;
            this.lock = cell.lock;
            this.data = cell.data;
        }

        public Builder setCapacity(BigInteger capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder setCapacity(String capacity) {
            return setCapacity(Hex.toBigInteger(capacity));
        }

        public Builder setType(Script type) {
            this.type = type;
            return this;
        }

        public Builder setLock(Script lock) {
            this.lock = lock;
            return this;
        }

        public Builder setData(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder setData(String data) {
            setData(Hex.toByteArray(data));
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
}
