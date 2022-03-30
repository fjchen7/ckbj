package org.ckbj.type;


/**
 * Live cell to be consumed in {@link Transaction}.
 */
public class CellInput {
    private OutPoint previousOutput;
    // TODO: implement class to parse since
    /**
     * 8-byte length field to prevent {@link Transaction} to be mined before an absolute or relative time.
     * @see <a href="https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0017-tx-valid-since/0017-tx-valid-since.md">RFC17 Transaction valid since</a>
     */
    private byte[] since;

    private CellInput() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CellInput clone) {
        return new Builder(clone);
    }

    public OutPoint getPreviousOutput() {
        return previousOutput;
    }

    public byte[] getSince() {
        return since;
    }

    public static final class Builder {
        private OutPoint previousOutput;
        // TODO: implement class to parse since
        private byte[] since;

        private Builder() {
            since = new byte[]{};
        }

        public Builder(CellInput clone) {
            this.previousOutput = clone.previousOutput;
            this.since = clone.since;
        }

        public Builder setPreviousOutput(OutPoint previousOutput) {
            this.previousOutput = previousOutput;
            return this;
        }

        public Builder setPreviousOutput(byte[] txHash, int index) {
            OutPoint previousOutput = OutPoint.build()
                    .setTxHash(txHash)
                    .setIndex(index)
                    .build();
            this.previousOutput = previousOutput;
            return this;
        }

        public Builder setSince(byte[] since) {
            this.since = since;
            return this;
        }

        public CellInput build() {
            CellInput cellInput = new CellInput();
            cellInput.since = this.since;
            cellInput.previousOutput = this.previousOutput;
            return cellInput;
        }
    }
}
