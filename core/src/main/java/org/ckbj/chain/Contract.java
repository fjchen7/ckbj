package org.ckbj.chain;

import org.ckbj.type.CellDep;
import org.ckbj.type.Script;
import org.ckbj.utils.Hex;

import java.util.ArrayList;
import java.util.List;

public class Contract {
    protected String name;
    protected Script.HashType hashType = Script.HashType.TYPE;
    protected byte[] codeHash;
    protected List<CellDep> cellDeps = new ArrayList<>();

    public Contract(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Script.HashType getHashType() {
        return hashType;
    }

    public byte[] getCodeHash() {
        return codeHash;
    }

    public List<CellDep> getCellDeps() {
        return cellDeps;
    }

    public Contract setName(String name) {
        this.name = name;
        return this;
    }

    public Contract setHashType(Script.HashType hashType) {
        this.hashType = hashType;
        return this;
    }

    public Contract setCodeHash(String codeHash) {
        return setCodeHash(Hex.decode(codeHash));
    }

    public Contract setCellDeps(List<CellDep> cellDeps) {
        this.cellDeps = cellDeps;
        return this;
    }

    public Contract setCodeHash(byte[] codeHash) {
        this.codeHash = codeHash;
        return this;
    }

    public Contract addCellDep(CellDep cellDep) {
        this.cellDeps.add(cellDep);
        return this;
    }
}
