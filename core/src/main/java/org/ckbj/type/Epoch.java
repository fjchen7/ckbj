package org.ckbj.type;

import com.google.gson.annotations.SerializedName;

public class Epoch {
    private int number;
    @SerializedName("start_number")
    private int startBlockNumber;
    private int length;
    private long compactTarget;

    private Epoch() {
    }

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
}
