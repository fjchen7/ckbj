package org.ckbj.type;

import java.util.ArrayList;
import java.util.List;

public class UncleBlock {
    private Header header;
    private List<byte[]> proposals = new ArrayList<>();

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<byte[]> getProposals() {
        return proposals;
    }

    public void setProposals(List<byte[]> proposals) {
        this.proposals = proposals;
    }
}
