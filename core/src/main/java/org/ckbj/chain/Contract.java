package org.ckbj.chain;

import org.ckbj.type.CellDep;
import org.ckbj.type.Script;
import org.ckbj.utils.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ckbj.type.Script.Type.LOCK;
import static org.ckbj.type.Script.Type.TYPE;

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

    public enum Type {
        SECP256K1_BLAKE160_SIGHASH_ALL(LOCK),
        SECP256K1_BLAKE160_MULTISIG_ALL(LOCK),
        ANYONE_CAN_PAY(LOCK),
        PORTAL_WALLET_LOCK(LOCK),
        CHEQUE(LOCK),
        SUDT(TYPE),
        DAO(TYPE),
        TYPE_ID(TYPE);

        private Script.Type scriptType;

        Type(Script.Type scriptType) {
            this.scriptType = scriptType;
        }

        public Script.Type getScriptType() {
            return scriptType;
        }
    }
}
