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

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStartBlockNumber() {
        return startBlockNumber;
    }

    public void setStartBlockNumber(int startBlockNumber) {
        this.startBlockNumber = startBlockNumber;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getCompactTarget() {
        return compactTarget;
    }

    public void setCompactTarget(long compactTarget) {
        this.compactTarget = compactTarget;
    }
}
