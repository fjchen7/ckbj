package org.ckbj.type;

import java.util.List;

public class Contract {
    protected List<CellDep> cellDeps;
    protected byte[] codeHash;

    private Contract() {
    }

    public List<CellDep> getCellDeps() {
        return cellDeps;
    }

    public byte[] getCodeHash() {
        return codeHash;
    }

    public static final class Builder {
        protected List<CellDep> cellDeps;
        protected byte[] codeHash;

        private Builder() {
        }

        public Builder addCellDep(byte[] txHash, int index, CellDep.DepType depType) {
            CellDep cellDep = CellDep.builder()
                    .setOutPoint(txHash, index)
                    .setDepType(depType)
                    .build();
            this.cellDeps.add(cellDep);
            return this;
        }

        public Builder addCellDep(CellDep cellDep) {
            this.cellDeps.add(cellDep);
            return this;
        }

        public Builder setCellDeps(List<CellDep> CellDeps) {
            this.cellDeps = CellDeps;
            return this;
        }

        public Builder setCodeHash(byte[] codeHash) {
            this.codeHash = codeHash;
            return this;
        }

        public Contract build() {
            Contract contract = new Contract();
            contract.cellDeps = this.cellDeps;
            contract.codeHash = this.codeHash;
            return contract;
        }
    }
}
