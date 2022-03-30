package org.ckbj.type;

import java.util.ArrayList;
import java.util.List;

public class UncleBlock {
    private Header header;
    private List<byte[]> proposals = new ArrayList<>();

    public Header getHeader() {
        return header;
    }

    public byte[] getProposal(int i) {
        return proposals.get(i);
    }

    public List<byte[]> getProposals() {
        return proposals;
    }

    public UncleBlock setHeader(Header header) {
        this.header = header;
        return this;
    }

    public UncleBlock setProposals(List<byte[]> proposals) {
        this.proposals = proposals;
        return this;
    }
}
