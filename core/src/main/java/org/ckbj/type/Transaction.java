package org.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import org.ckbj.crypto.Blake2b;
import org.ckbj.molecule.Serializer;
import org.ckbj.utils.Hex;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Core CKB transaction data structure
 */
@JsonAdapter(Transaction.TypeAdapter.class)
public class Transaction {
    private int version;
    private List<CellDep> cellDeps = new ArrayList<>();
    private List<byte[]> headerDeps = new ArrayList<>();
    private List<CellInput> inputs = new ArrayList<>();
    private List<Cell> outputs = new ArrayList<>();
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
        List<byte[]> outputsData = new ArrayList<>();
        for (int i = 0; i < outputs.size(); i++) {
            outputsData.add(outputs.get(i).getData());
        }
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
        return addHeaderDep(Hex.toByteArray(headerDep));
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

    public Transaction addWitness(String witness) {
        return addWitness(Hex.toByteArray(witness));
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

    protected static class TypeAdapter implements JsonDeserializer<Transaction> {
        @Override
        public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            Transaction tx = new Transaction();
            tx.version = context.deserialize(obj.get("version"), int.class);
            tx.cellDeps = context.deserialize(obj.get("cell_deps"), new TypeToken<List<CellDep>>() {}.getType());
            tx.headerDeps = context.deserialize(obj.get("header_deps"), new TypeToken<List<byte[]>>() {}.getType());
            tx.inputs = context.deserialize(obj.get("inputs"), new TypeToken<List<CellInput>>() {}.getType());
            tx.outputs = context.deserialize(obj.get("outputs"), new TypeToken<List<Cell>>() {}.getType());
            List<byte[]> outputsData = context.deserialize(obj.get("outputs_data"), new TypeToken<List<byte[]>>() {}.getType());
            for (int i = 0; i < tx.outputs.size(); i++) {
                tx.outputs.get(i).setData(outputsData.get(i));
            }
            tx.witnesses = context.deserialize(obj.get("witnesses"), new TypeToken<List<byte[]>>() {}.getType());
            return tx;
        }
    }
}
