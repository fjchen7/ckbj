package org.ckbj.type;

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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<byte[]> getProposals() {
        return proposals;
    }

    public List<UncleBlock> getUncleBlocks() {
        return uncleBlocks;
    }


    public Block setHeader(Header header) {
        this.header = header;
        return this;
    }

    public Block setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Block setProposals(List<byte[]> proposals) {
        this.proposals = proposals;
        return this;
    }

    public Block setUncleBlocks(List<UncleBlock> uncleBlocks) {
        this.uncleBlocks = uncleBlocks;
        return this;
    }
}
