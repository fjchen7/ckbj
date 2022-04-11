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

    public void setPreviousOutput(OutPoint previousOutput) {
        this.previousOutput = previousOutput;
    }

    public byte[] getSince() {
        return since;
    }

    public void setSince(byte[] since) {
        this.since = since;
    }
}
