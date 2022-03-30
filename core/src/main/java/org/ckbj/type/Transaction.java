package org.ckbj.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Core CKB transaction data structure
 */
public class Transaction {
    private int version;
    private List<CellDep> cellDeps;
    private List<byte[]> headerDeps;
    private List<CellInput> inputs;
    private List<Cell> outputs;
    private List<byte[]> outputsData;
    private List<byte[]> witnesses;

    private Transaction() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Transaction clone) {
        return new Builder(clone);
    }

    public int getVersion() {
        return version;
    }

    public List<CellDep> getCellDeps() {
        return cellDeps;
    }

    public List<byte[]> getHeaderDeps() {
        return headerDeps;
    }

    public List<CellInput> getInputs() {
        return inputs;
    }

    public List<Cell> getOutputs() {
        return outputs;
    }

    public List<byte[]> getOutputsData() {
        return outputsData;
    }

    public List<byte[]> getWitnesses() {
        return witnesses;
    }

    // TODO
    public byte[] hash() {
        return null;
//    Blake2b blake2b = new Blake2b();
//    blake2b.update(Encoder.encode(Serializer.serializeRawTransaction(this)));
//    return blake2b.doFinalBytes();
    }

    public static final class Builder {
        private List<CellDep> cellDeps;
        private List<byte[]> headerDeps;
        private List<CellInput> inputs;
        private List<Cell> outputs;
        private List<byte[]> outputsData;
        private List<byte[]> witnesses;
        private int version;

        private Builder() {
            cellDeps = new ArrayList<>();
            headerDeps = new ArrayList<>();
            inputs = new ArrayList<>();
            outputs = new ArrayList<>();
            outputsData = new ArrayList<>();
            witnesses = new ArrayList<>();
        }

        public Builder(Transaction clone) {
            this.cellDeps = clone.cellDeps;
            this.headerDeps = clone.headerDeps;
            this.inputs = clone.inputs;
            this.outputs = clone.outputs;
            this.outputsData = clone.outputsData;
            this.witnesses = clone.witnesses;
            this.version = clone.version;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder addCellDep(CellDep cellDep) {
            Objects.requireNonNull(cellDep);
            this.cellDeps.add(cellDep);
            return this;
        }

        public Builder setCellDeps(List<CellDep> cellDeps) {
            Objects.requireNonNull(cellDeps);
            this.cellDeps = cellDeps;
            return this;
        }

        public Builder addHeaderDep(byte[] headerDep) {
            if (headerDep.length != 32) {
                throw new IllegalArgumentException("headerDep should be 32 bytes");
            }
            this.headerDeps.add(headerDep);
            return this;
        }

        public Builder setHeaderDeps(List<byte[]> headerDeps) {
            for (byte[] headerDep : headerDeps) {
                if (headerDep.length != 32) {
                    throw new IllegalArgumentException("headerDep should be 32 bytes");
                }
            }
            this.headerDeps = headerDeps;
            return this;
        }

        public Builder addInput(OutPoint outPoint) {
            return addInput(outPoint, new byte[]{});
        }

        public Builder addInput(OutPoint outPoint, byte[] since) {
            CellInput input = CellInput.builder()
                    .setPreviousOutput(outPoint)
                    .setSince(since)
                    .build();
            this.inputs.add(input);
            return this;
        }

        public Builder addInput(CellInput input) {
            this.inputs.add(input);
            return this;
        }

        public Builder setInputs(List<CellInput> inputs) {
            Objects.requireNonNull(inputs);
            this.inputs = inputs;
            return this;
        }

        public Builder addOutput(Cell output) {
            this.outputs.add(output);
            return this;
        }

        public Builder setOutputs(List<Cell> outputs) {
            Objects.requireNonNull(outputs);
            this.outputs = outputs;
            return this;
        }

        public Builder addOutputData(byte[] outputData) {
            this.outputsData.add(outputData);
            return this;
        }

        public Builder setOutputsData(List<byte[]> outputsData) {
            Objects.requireNonNull(outputsData);
            this.outputsData = outputsData;
            return this;
        }

        public Builder addWitness(byte[] witness) {
            this.witnesses.add(witness);
            return this;
        }

        public Builder setWitnesses(List<byte[]> witnesses) {
            Objects.requireNonNull(witnesses);
            this.witnesses = witnesses;
            return this;
        }

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.version = this.version;
            transaction.cellDeps = this.cellDeps;
            transaction.outputsData = this.outputsData;
            transaction.witnesses = this.witnesses;
            transaction.headerDeps = this.headerDeps;
            transaction.outputs = this.outputs;
            transaction.inputs = this.inputs;
            return transaction;
        }
    }
}
