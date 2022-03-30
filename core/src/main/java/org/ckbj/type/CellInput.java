package org.ckbj.type;


/**
 * Live cell to be consumed in {@link Transaction}.
 */
public class CellInput {
    private OutPoint previousOutput;
    // TODO: implement class to parse since
    /**
     * 8-byte length field to prevent {@link Transaction} to be mined before an absolute or relative time.
     *
     * @see <a href="https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0017-tx-valid-since/0017-tx-valid-since.md">RFC17 Transaction valid since</a>
     */
    private byte[] since;

    public CellInput(OutPoint previousOutput, byte[] since) {
        this.previousOutput = previousOutput;
        this.since = since;
    }

    public CellInput(OutPoint previousOutput) {
        this(previousOutput, new byte[]{0});
    }

    public OutPoint getPreviousOutput() {
        return previousOutput;
    }

    public byte[] getSince() {
        return since;
    }

    public CellInput setPreviousOutput(OutPoint previousOutput) {
        this.previousOutput = previousOutput;
        return this;
    }

    public CellInput setPreviousOutput(byte[] txHash, int index) {
        this.previousOutput = new OutPoint(txHash, index);
        return this;
    }

    public CellInput setSince(byte[] since) {
        this.since = since;
        return this;
    }
}
