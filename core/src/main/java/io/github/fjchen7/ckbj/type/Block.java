package io.github.fjchen7.ckbj.type;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private Header header;
    private List<Transaction> transactions = new ArrayList<>();
    private List<byte[]> proposals = new ArrayList<>();
    @SerializedName("uncles")
    private List<UncleBlock> uncleBlocks = new ArrayList<>();

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<byte[]> getProposals() {
        return proposals;
    }

    public void setProposals(List<byte[]> proposals) {
        this.proposals = proposals;
    }

    public List<UncleBlock> getUncleBlocks() {
        return uncleBlocks;
    }

    public void setUncleBlocks(List<UncleBlock> uncleBlocks) {
        this.uncleBlocks = uncleBlocks;
    }
}
