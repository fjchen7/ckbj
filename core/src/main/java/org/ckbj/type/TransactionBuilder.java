package org.ckbj.type;

import org.ckbj.utils.Hex;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TransactionBuilder {

    private int version = 0;
    private List<CellDep> cellDeps = new ArrayList<>();
    private List<byte[]> headerDeps = new ArrayList<>();
    private List<CellInput> inputs = new ArrayList<>();
    private List<Cell> outputs = new ArrayList<>();
    private List<byte[]> witnesses = new ArrayList<>();

    TransactionBuilder() {
    }

    public TransactionBuilder setVersion(int version) {
        this.version = version;
        return this;
    }

    public TransactionBuilder setCellDeps(List<CellDep> cellDeps) {
        Objects.requireNonNull(cellDeps);
        this.cellDeps = cellDeps;
        return this;
    }

    public TransactionBuilder addCellDep(CellDep cellDep) {
        Objects.requireNonNull(cellDep);
        this.cellDeps.add(cellDep);
        return this;
    }

    public TransactionBuilder addCellDep(CellDep.DepType depType, String txHash, int index) {
        return addCellDep(depType, Hex.toByteArray(txHash), index);
    }

    public TransactionBuilder addCellDep(CellDep.DepType depType, byte[] txHash, int index) {
        CellDep cellDep = new CellDep();
        cellDep.setDepType(depType);
        cellDep.setOutPoint(new OutPoint(txHash, index));
        return addCellDep(cellDep);
    }

    public TransactionBuilder setHeaderDeps(List<byte[]> headerDeps) {
        Objects.requireNonNull(headerDeps);
        this.headerDeps = headerDeps;
        return this;
    }

    public TransactionBuilder addHeaderDep(byte[] headerDep) {
        if (headerDep.length != 32) {
            throw new IllegalArgumentException("headerDep length must be 32");
        }
        this.headerDeps.add(headerDep);
        return this;
    }

    public TransactionBuilder setInputs(List<CellInput> inputs) {
        Objects.requireNonNull(inputs);
        this.inputs = inputs;
        return this;
    }

    public TransactionBuilder addInput(CellInput inputs) {
        Objects.requireNonNull(inputs);
        this.inputs.add(inputs);
        return this;
    }

    public TransactionBuilder addInput(byte[] txHash, int index) {
        CellInput cellInput = new CellInput(new OutPoint(txHash, index));
        return addInput(cellInput);
    }

    public TransactionBuilder addInput(String txHash, int index) {
        return addInput(Hex.toByteArray(txHash), index);
    }

    public TransactionBuilder setOutputs(List<Cell> outputs) {
        Objects.requireNonNull(outputs);
        this.outputs = outputs;
        return this;
    }

    public TransactionBuilder addOutput(Cell output) {
        Objects.requireNonNull(output);
        this.outputs.add(output);
        return this;
    }

    public TransactionBuilder addWitness() {
        return addWitness(new byte[0]);
    }

    public TransactionBuilder addWitness(byte[] witness) {
        Objects.requireNonNull(witness);
        this.witnesses.add(witness);
        return this;
    }

    public TransactionBuilder addWitnesses(String witness) {
        return addWitness(Hex.toByteArray(witness));
    }

    public Transaction build() {
        if (witnesses.size() < inputs.size()) {
            throw new IllegalArgumentException("witnesses length must be greater than or equal to inputs length");
        }
        removeDuplicateCellDeps();
        Transaction transaction = new Transaction();
        transaction.setVersion(version);
        transaction.setCellDeps(new ArrayList<>(cellDeps));
        transaction.setHeaderDeps(headerDeps);
        transaction.setInputs(inputs);
        transaction.setOutputs(outputs);
        transaction.setWitnesses(witnesses);
        return transaction;
    }

    private void removeDuplicateCellDeps() {
        List<CellDep> unique = new ArrayList<>();
        for (CellDep cellDep: this.cellDeps) {
            if (!unique.contains(cellDep)) {
                unique.add(cellDep);
            }
        }
        this.cellDeps = unique;
    }
}
