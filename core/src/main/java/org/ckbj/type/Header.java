package org.ckbj.type;

import java.math.BigInteger;

public class Header {
    private int version;
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

    private Header() {
    }

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

    public BigInteger getNonce() {
        return nonce;
    }

}
