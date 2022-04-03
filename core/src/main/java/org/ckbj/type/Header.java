package org.ckbj.type;

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
    private byte[] nonce;

    public int getVersion() {
        return version;
    }

    public long getCompactTarget() {
        return compactTarget;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getNumber() {
        return number;
    }

    public EpochFraction getEpoch() {
        return epoch;
    }

    public byte[] getParentHash() {
        return parentHash;
    }

    public byte[] getTransactionsRoot() {
        return transactionsRoot;
    }

    public byte[] getProposalsHash() {
        return proposalsHash;
    }

    public byte[] getExtraHash() {
        return extraHash;
    }

    public byte[] getDao() {
        return dao;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public Header setVersion(int version) {
        this.version = version;
        return this;
    }

    public Header setCompactTarget(long compactTarget) {
        this.compactTarget = compactTarget;
        return this;
    }

    public Header setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Header setNumber(int number) {
        this.number = number;
        return this;
    }

    public Header setEpoch(EpochFraction epoch) {
        this.epoch = epoch;
        return this;
    }

    public Header setParentHash(byte[] parentHash) {
        this.parentHash = parentHash;
        return this;
    }

    public Header setTransactionsRoot(byte[] transactionsRoot) {
        this.transactionsRoot = transactionsRoot;
        return this;
    }

    public Header setProposalsHash(byte[] proposalsHash) {
        this.proposalsHash = proposalsHash;
        return this;
    }

    public Header setExtraHash(byte[] extraHash) {
        this.extraHash = extraHash;
        return this;
    }

    public Header setDao(byte[] dao) {
        this.dao = dao;
        return this;
    }

    public Header setNonce(byte[] nonce) {
        this.nonce = nonce;
        return this;
    }
}
