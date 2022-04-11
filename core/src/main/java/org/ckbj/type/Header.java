package org.ckbj.type;

import org.ckbj.crypto.Blake2b;
import org.ckbj.molecule.Serializer;

import java.math.BigInteger;

public class Header {
    private int version = 0;
    private long compactTarget;
    private long timestamp;
    private int number;
    private EpochFraction epoch;
    private byte[] parentHash;
    private byte[] transactionsRoot;
    private byte[] proposalsHash;
    private byte[] extraHash;
    // TODO: implement class to parse dao
    private byte[] dao;
    private BigInteger nonce;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getCompactTarget() {
        return compactTarget;
    }

    public void setCompactTarget(long compactTarget) {
        this.compactTarget = compactTarget;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public EpochFraction getEpoch() {
        return epoch;
    }

    public void setEpoch(EpochFraction epoch) {
        this.epoch = epoch;
    }

    public byte[] getParentHash() {
        return parentHash;
    }

    public void setParentHash(byte[] parentHash) {
        this.parentHash = parentHash;
    }

    public byte[] getTransactionsRoot() {
        return transactionsRoot;
    }

    public void setTransactionsRoot(byte[] transactionsRoot) {
        this.transactionsRoot = transactionsRoot;
    }

    public byte[] getProposalsHash() {
        return proposalsHash;
    }

    public void setProposalsHash(byte[] proposalsHash) {
        this.proposalsHash = proposalsHash;
    }

    public byte[] getExtraHash() {
        return extraHash;
    }

    public void setExtraHash(byte[] extraHash) {
        this.extraHash = extraHash;
    }

    public byte[] getDao() {
        return dao;
    }

    public void setDao(byte[] dao) {
        this.dao = dao;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public byte[] hash() {
        byte[] serialization = Serializer.serialize(this, true);
        return Blake2b.digest(serialization);
    }
}
