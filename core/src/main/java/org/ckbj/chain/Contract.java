package org.ckbj.chain;

import org.ckbj.type.CellDep;
import org.ckbj.type.Script;
import org.ckbj.utils.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        SECP256K1_BLAKE160_SIGHASH_ALL(true, false),
        SECP256K1_BLAKE160_MULTISIG_ALL(true, false),
        ANYONE_CAN_PAY(true, false),
        PORTAL_WALLET_LOCK(true, false),
        CHEQUE(true, false),
        SUDT(false, true),
        DAO(false, true),
        TYPE_ID(false, true);

        private boolean isLock;
        private boolean isType;

        Type(boolean isLock, boolean isType) {
            this.isLock = isLock;
            this.isType = isType;
        }

        public boolean isLock() {
            return isLock;
        }

        public boolean isType() {
            return isType;
        }
    }
}
