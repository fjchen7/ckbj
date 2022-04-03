package org.ckbj.type;

import org.ckbj.crypto.Blake2b;
import org.ckbj.molecule.Serializer;
import org.ckbj.utils.Hex;

import java.util.ArrayList;
import java.util.List;

/**
 * Core CKB transaction data structure
 */
public class Transaction {
    private int version;
    private List<CellDep> cellDeps = new ArrayList<>();
    private List<byte[]> headerDeps = new ArrayList<>();
    private List<CellInput> inputs = new ArrayList<>();
    private List<Cell> outputs = new ArrayList<>();
    private List<byte[]> outputsData = new ArrayList<>();
    private List<byte[]> witnesses = new ArrayList<>();

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

    public Transaction setVersion(int version) {
        this.version = version;
        return this;
    }

    public Transaction addCellDep(CellDep cellDep) {
        this.cellDeps.add(cellDep);
        return this;
    }

    public Transaction setCellDeps(List<CellDep> cellDeps) {
        this.cellDeps = cellDeps;
        return this;
    }

    public Transaction addHeaderDep(String headerDep) {
        return addHeaderDep(Hex.decode(headerDep));
    }

    public Transaction addHeaderDep(byte[] headerDep) {
        if (headerDep.length != 32) {
            throw new IllegalArgumentException("headerDep should be 32 bytes");
        }
        this.headerDeps.add(headerDep);
        return this;
    }

    public Transaction setHeaderDeps(List<byte[]> headerDeps) {
        for (byte[] headerDep : headerDeps) {
            if (headerDep.length != 32) {
                throw new IllegalArgumentException("headerDep should be 32 bytes");
            }
        }
        this.headerDeps = headerDeps;
        return this;
    }

    public Transaction addInput(OutPoint outPoint) {
        this.inputs.add(new CellInput(outPoint));
        return this;
    }

    public Transaction addInput(OutPoint outPoint, byte[] since) {
        this.inputs.add(new CellInput(outPoint, since));
        return this;
    }

    public Transaction addInput(CellInput input) {
        this.inputs.add(input);
        return this;
    }

    public Transaction setInputs(List<CellInput> inputs) {
        this.inputs = inputs;
        return this;
    }

    public Transaction addOutput(Cell output) {
        this.outputs.add(output);
        return this;
    }

    public Transaction setOutputs(List<Cell> outputs) {
        this.outputs = outputs;
        return this;
    }

    public Transaction addOutputData(String outputData) {
        return addOutputData(Hex.decode(outputData));
    }

    public Transaction addOutputData(byte[] outputData) {
        this.outputsData.add(outputData);
        return this;
    }

    public Transaction setOutputsData(List<byte[]> outputsData) {
        this.outputsData = outputsData;
        return this;
    }

    public Transaction addWitness(String witness) {
        return addWitness(Hex.decode(witness));
    }

    public Transaction addWitness(byte[] witness) {
        this.witnesses.add(witness);
        return this;
    }

    public Transaction setWitnesses(List<byte[]> witnesses) {
        this.witnesses = witnesses;
        return this;
    }

    public byte[] hash() {
        byte[] serialization = Serializer.serialize(this, false);
        return Blake2b.digest256(serialization);
    }
}
