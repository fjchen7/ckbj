package org.ckbj.type;

import java.math.BigInteger;

public class Cell {
    private BigInteger capacity;
    private Script type;
    private Script lock;

    private Cell() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Cell clone) {
        return new Builder(clone);
    }

    public BigInteger getCapacity() {
        return capacity;
    }

    public Script getType() {
        return type;
    }

    public Script getLock() {
        return lock;
    }

    public static class Builder {
        private BigInteger capacity;
        private Script type;
        private Script lock;

        private Builder() {
            capacity = BigInteger.ZERO;
        }

        public Builder(Cell clone) {
            this.capacity = clone.capacity;
            this.type = clone.type;
            this.lock = clone.lock;
        }

        public Builder setCapacityInBytes(int capacityInBytes) {
            // TODO: convert bytes to shannon
            this.capacity = capacity;
            return this;
        }

        public Builder setCapacity(BigInteger capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder setType(Script type) {
            this.type = type;
            return this;
        }

        public Builder setLock(Script lock) {
            this.lock = lock;
            return this;
        }

        public Cell build() {
            Cell cell = new Cell();
            cell.capacity = this.capacity;
            cell.type = this.type;
            cell.lock = this.lock;
            return cell;
        }
    }
}
