package org.ckbj.type;

import org.ckbj.utils.Hex;

import java.util.ArrayList;
import java.util.List;

public class Contract {
    protected List<CellDep> cellDeps = new ArrayList<>();
    protected byte[] codeHash;

    public Contract(byte[] codeHash) {
        setCodeHash(codeHash);
    }

    public Contract(String codeHash) {
        setCodeHash(codeHash);
    }

    public List<CellDep> getCellDeps() {
        return cellDeps;
    }

    public byte[] getCodeHash() {
        return codeHash;
    }

    public Contract addCellDep(CellDep cellDep) {
        this.cellDeps.add(cellDep);
        return this;
    }

    public Contract setCellDeps(List<CellDep> cellDeps) {
        this.cellDeps = cellDeps;
        return this;
    }

    public Contract setCodeHash(byte[] codeHash) {
        this.codeHash = codeHash;
        return this;
    }

    public Contract setCodeHash(String codeHash) {
        return setCodeHash(Hex.decode(codeHash));
    }
}
