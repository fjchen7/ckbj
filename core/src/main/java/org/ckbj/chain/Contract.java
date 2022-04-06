package org.ckbj.chain;

import org.ckbj.type.CellDep;
import org.ckbj.type.Script;
import org.ckbj.utils.Hex;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Contract {
    protected String name;
    protected byte[] codeHash;
    protected Script.HashType hashType = Script.HashType.TYPE;
    protected List<CellDep> cellDeps = new ArrayList<>();

    public Contract(String name) {
        setName(name);
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
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }

    public Contract setHashType(Script.HashType hashType) {
        Objects.requireNonNull(hashType);
        this.hashType = hashType;
        return this;
    }

    public Contract setCodeHash(String codeHash) {
        return setCodeHash(Hex.toByteArray(codeHash));
    }

    public Contract setCellDeps(List<CellDep> cellDeps) {
        this.cellDeps = cellDeps;
        return this;
    }

    public Contract setCodeHash(byte[] codeHash) {
        Objects.requireNonNull(codeHash);
        this.codeHash = codeHash;
        return this;
    }

    public Contract addCellDep(CellDep cellDep) {
        this.cellDeps.add(cellDep);
        return this;
    }

    public Script createScript(byte[] args) {
        return new Script()
                .setCodeHash(codeHash)
                .setArgs(args)
                .setHashType(hashType);
    }

    public enum Standard {
        SECP256K1_BLAKE160_SIGHASH_ALL(true, false),
        SECP256K1_BLAKE160_MULTISIG_ALL(true, false),
        ANYONE_CAN_PAY(true, false),
        PORTAL_WALLET_LOCK(true, false),
        CHEQUE(true, false),
        SUDT(false, true),
        DAO(false, true),
        TYPE_ID(false, true),
        UNKNOWN(false, false);

        private boolean isLock;
        private boolean isType;

        Standard(boolean isLock, boolean isType) {
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
