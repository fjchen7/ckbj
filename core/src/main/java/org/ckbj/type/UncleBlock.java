package org.ckbj.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UncleBlock {
    private Header header;
    private List<byte[]> proposals;

    private UncleBlock() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Header getHeader() {
        return header;
    }

    public byte[] getProposal(int i) {
        return proposals.get(i);
    }

    public List<byte[]> getProposals() {
        return proposals;
    }

    public static final class Builder {
        private Header header;
        private List<byte[]> proposals;

        private Builder() {
            proposals = new ArrayList<>();
        }

        public Builder setHeader(Header header) {
            this.header = header;
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

        public UncleBlock build() {
            UncleBlock uncleBlock = new UncleBlock();
            uncleBlock.proposals = this.proposals;
            uncleBlock.header = this.header;
            return uncleBlock;
        }
    }
}
