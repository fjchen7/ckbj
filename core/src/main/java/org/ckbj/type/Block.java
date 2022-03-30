package org.ckbj.type;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block {
    private Header header;
    private List<Transaction> transactions;
    private List<byte[]> proposals;
    @SerializedName("uncles")
    private List<UncleBlock> uncleBlocks;

    private Block() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Header getHeader() {
        return header;
    }

    public Transaction getTransactions(int i) {
        return transactions.get(i);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public byte[] getProposal(int i) {
        return proposals.get(i);
    }

    public List<byte[]> getProposals() {
        return proposals;
    }

    public UncleBlock getUncleBlock(int i) {
        return uncleBlocks.get(i);
    }

    public List<UncleBlock> getUncleBlocks() {
        return uncleBlocks;
    }

    public static final class Builder {
        private Header header;
        private List<Transaction> transactions;
        private List<byte[]> proposals;
        private List<UncleBlock> uncleBlocks;

        private Builder() {
            transactions = new ArrayList<>();
            proposals = new ArrayList<>();
            uncleBlocks = new ArrayList<>();
        }

        public Builder setHeader(Header header) {
            this.header = header;
            return this;
        }

        public Builder addTransaction(Transaction transaction) {
            this.transactions.add(transaction);
            return this;
        }

        public Builder setTransactions(List<Transaction> transactions) {
            Objects.requireNonNull(transactions);
            this.transactions = transactions;
            return this;
        }

        public Builder addProposal(byte[] proposal) {
            if (proposal.length != 10) {
                throw new IllegalArgumentException("proposal should be 10 bytes");
            }
            this.proposals.add(proposal);
            return this;
        }

        public Builder setProposals(List<byte[]> proposals) {
            Objects.requireNonNull(proposals);
            for (byte[] proposal : proposals) {
                if (proposal.length != 10) {
                    throw new IllegalArgumentException("proposal should be 10 bytes");
                }
            }
            this.proposals = proposals;
            return this;
        }

        public Builder addUncleBlock(UncleBlock uncleBlock) {
            this.uncleBlocks.add(uncleBlock);
            return this;
        }

        public Builder setUncleBlocks(List<UncleBlock> uncleBlocks) {
            Objects.requireNonNull(uncleBlocks);
            this.uncleBlocks = uncleBlocks;
            return this;
        }

        public Block build() {
            Block block = new Block();
            block.proposals = this.proposals;
            block.transactions = this.transactions;
            block.header = this.header;
            block.uncleBlocks = this.uncleBlocks;
            return block;
        }
    }
}
