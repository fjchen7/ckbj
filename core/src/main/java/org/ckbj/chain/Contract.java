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

    public enum Standard {
        SECP256K1_BLAKE160_SIGHASH_ALL,
        SECP256K1_BLAKE160_MULTISIG_ALL,
        ANYONE_CAN_PAY,
        PORTAL_WALLET_LOCK,
        CHEQUE,
        SUDT,
        DAO,
        TYPE_ID;
    }
}
