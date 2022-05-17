package org.ckbj.chain;

import org.ckbj.type.CellDep;
import org.ckbj.type.Script;
import org.ckbj.utils.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.ckbj.type.Script.Type.LOCK;
import static org.ckbj.type.Script.Type.TYPE;

public class Contract {
    private byte[] codeHash;
    private Script.HashType hashType = Script.HashType.TYPE;
    private List<CellDep> cellDeps = new ArrayList<>();

    public Script.HashType getHashType() {
        return hashType;
    }

    public Contract setHashType(Script.HashType hashType) {
        Objects.requireNonNull(hashType);
        this.hashType = hashType;
        return this;
    }

    public byte[] getCodeHash() {
        return codeHash;
    }

    public Contract setCodeHash(String codeHash) {
        return setCodeHash(Hex.toByteArray(codeHash));
    }

    public Contract setCodeHash(byte[] codeHash) {
        Objects.requireNonNull(codeHash);
        this.codeHash = codeHash;
        return this;
    }

    public List<CellDep> getCellDeps() {
        return cellDeps;
    }

    public Contract setCellDeps(List<CellDep> cellDeps) {
        this.cellDeps = cellDeps;
        return this;
    }

    public Contract addCellDep(CellDep cellDep) {
        this.cellDeps.add(cellDep);
        return this;
    }

    /**
     * Check if the script uses code of given contract.
     *
     * @return true if the script uses code of given contract.
     */
    public boolean usedBy(Script script) {
        return Arrays.equals(script.getCodeHash(), getCodeHash());
    }

    public Script createScript(byte[] args) {
        return Script.builder()
                .setCodeHash(codeHash)
                .setArgs(args)
                .setHashType(hashType)
                .build();
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
