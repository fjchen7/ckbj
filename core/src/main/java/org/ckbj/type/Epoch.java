package org.ckbj.type;

import com.google.gson.annotations.SerializedName;

public class Epoch {
    private int number;
    @SerializedName("start_number")
    private int startBlockNumber;
    private int length;
    private long compactTarget;

    public int getNumber() {
        return number;
    }

    public int getStartBlockNumber() {
        return startBlockNumber;
    }

    public int getLength() {
        return length;
    }

    public long getCompactTarget() {
        return compactTarget;
    }

    public Epoch setNumber(int number) {
        this.number = number;
        return this;
    }

    public Epoch setStartBlockNumber(int startBlockNumber) {
        this.startBlockNumber = startBlockNumber;
        return this;
    }

    public Epoch setLength(int length) {
        this.length = length;
        return this;
    }

    public Epoch setCompactTarget(long compactTarget) {
        this.compactTarget = compactTarget;
        return this;
    }
}
