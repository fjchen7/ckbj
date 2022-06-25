package io.github.fjchen7.ckbj.chain;

import io.github.fjchen7.ckbj.type.CellDep;
import io.github.fjchen7.ckbj.type.Script;
import io.github.fjchen7.ckbj.utils.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Contract {
    private byte[] codeHash;
    private Script.HashType hashType = Script.HashType.TYPE;
    private List<CellDep> cellDeps = new ArrayList<>();

    public Contract(byte[] codeHash, Script.HashType hashType, List<CellDep> cellDeps) {
        this.codeHash = codeHash;
        this.hashType = hashType;
        this.cellDeps = cellDeps;
    }

    public Contract(byte[] codeHash, Script.HashType hashType, CellDep... cellDeps) {
        this.codeHash = codeHash;
        this.hashType = hashType;
        this.cellDeps = Arrays.asList(cellDeps);
    }

    public byte[] getCodeHash() {
        return codeHash;
    }

    public Script.HashType getHashType() {
        return hashType;
    }

    public List<CellDep> getCellDeps() {
        return cellDeps;
    }

    public Script createScript(byte[] args) {
        Script script = new Script();
        script.setCodeHash(codeHash);
        script.setArgs(args);
        script.setHashType(hashType);
        return script;
    }

    public Script createScript(String args) {
        return createScript(Hex.toByteArray(args));
    }

    public enum Name {
        SECP256K1_BLAKE160_SIGHASH_ALL(Script.Type.LOCK),
        SECP256K1_BLAKE160_MULTISIG_ALL(Script.Type.LOCK),
        ANYONE_CAN_PAY(Script.Type.LOCK),
        PORTAL_WALLET_LOCK(Script.Type.LOCK),
        CHEQUE(Script.Type.LOCK),
        SUDT(Script.Type.TYPE),
        DAO(Script.Type.TYPE),
        TYPE_ID(Script.Type.TYPE);

        private Script.Type scriptType;

        Name(Script.Type scriptType) {
            this.scriptType = scriptType;
        }

        public Script.Type getScriptType() {
            return scriptType;
        }
    }
}
