package org.nervos.protocol.type;

/**
 * Core CKB transaction data structure
 */
public class Transaction {
    private long version;
    private CellDep[] cellDeps;
    private byte[][] headerDeps;
    private CellInput[] inputs;
    private byte[][] witness;
    private CellOutput[] outputs;
    private byte[][] outputData;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public CellDep[] getCellDeps() {
        return cellDeps;
    }

    public void setCellDeps(CellDep[] cellDeps) {
        this.cellDeps = cellDeps;
    }

    public byte[][] getHeaderDeps() {
        return headerDeps;
    }

    public void setHeaderDeps(byte[][] headerDeps) {
        this.headerDeps = headerDeps;
    }

    public CellInput[] getInputs() {
        return inputs;
    }

    public void setInputs(CellInput[] inputs) {
        this.inputs = inputs;
    }

    public byte[][] getWitness() {
        return witness;
    }

    public void setWitness(byte[][] witness) {
        this.witness = witness;
    }

    public CellOutput[] getOutputs() {
        return outputs;
    }

    public void setOutputs(CellOutput[] outputs) {
        this.outputs = outputs;
    }

    public byte[][] getOutputData() {
        return outputData;
    }

    public void setOutputData(byte[][] outputData) {
        this.outputData = outputData;
    }
}
