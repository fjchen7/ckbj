package org.ckbj;

import org.ckbj.protocol.type.CellDep;
import org.ckbj.protocol.type.Script;

import java.util.List;

public class ScriptInfo {
    protected List<CellDep> CellDeps;
    protected byte[] codeHash;
    protected Script.HashType hashType;

    public List<CellDep> getCellDeps() {
        return CellDeps;
    }

    public byte[] getCodeHash() {
        return codeHash;
    }

    public Script.HashType getHashType() {
        return hashType;
    }

    public ScriptInfo() {

    }

    public ScriptInfo(List<CellDep> cellDeps, byte[] codeHash, Script.HashType hashType) {
        CellDeps = cellDeps;
        this.codeHash = codeHash;
        this.hashType = hashType;
    }

    public static class ScriptInfoBuilder extends ScriptInfo {

        public ScriptInfoBuilder() {
            super();
        }

        public ScriptInfoBuilder(List<CellDep> cellDeps, byte[] codeHash, Script.HashType hashType) {
            super(cellDeps, codeHash, hashType);
        }

        public ScriptInfoBuilder addCellDep(String TxHash, int index, CellDep.DepType depType) {
            return this;
        }

        public ScriptInfoBuilder setCodeHash(String codeHash) {
            return this;
        }

        public ScriptInfoBuilder setHashType(Script.HashType hashType) {
            this.hashType = hashType;
            return this;
        }

        public ScriptInfo build() {
            return this;
        }
    }
}
