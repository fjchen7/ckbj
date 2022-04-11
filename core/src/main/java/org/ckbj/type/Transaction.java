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

    public static Builder builder() {
        return new Builder();
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<CellDep> getCellDeps() {
        return cellDeps;
    }

    public void setCellDeps(List<CellDep> cellDeps) {
        this.cellDeps = cellDeps;
    }

    public List<byte[]> getHeaderDeps() {
        return headerDeps;
    }

    public void setHeaderDeps(List<byte[]> headerDeps) {
        this.headerDeps = headerDeps;
    }

    public List<CellInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<CellInput> inputs) {
        this.inputs = inputs;
    }

    public List<Cell> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Cell> outputs) {
        this.outputs = outputs;
    }

    public List<byte[]> getOutputsData() {
        List<byte[]> outputsData = new ArrayList<>();
        for (Cell output : outputs) {
            outputsData.add(output.getData());
        }
        return outputsData;
    }

    public List<byte[]> getWitnesses() {
        return witnesses;
    }

    public void setWitnesses(List<byte[]> witnesses) {
        this.witnesses = witnesses;
    }

    public byte[] hash() {
        byte[] serialization = Serializer.serialize(this, false);
        return Blake2b.digest(serialization);
    }

    public static final class Builder {
        private int version;
        private List<CellDep> cellDeps = new ArrayList<>();
        private List<byte[]> headerDeps = new ArrayList<>();
        private List<CellInput> inputs = new ArrayList<>();
        private List<Cell> outputs = new ArrayList<>();
        private List<byte[]> witnesses = new ArrayList<>();

        private Builder() {
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder setCellDeps(List<CellDep> cellDeps) {
            this.cellDeps = cellDeps;
            return this;
        }

        public Builder addCellDep(CellDep cellDep) {
            this.cellDeps.add(cellDep);
            return this;
        }

        public Builder addCellDep(CellDep.DepType depType, String txHash, int index) {
            return addCellDep(depType, Hex.toByteArray(txHash), index);
        }

        public Builder addCellDep(CellDep.DepType depType, byte[] txHash, int index) {
            CellDep cellDep = CellDep.builder()
                    .setDepType(depType)
                    .setOutPoint(new OutPoint(txHash, index))
                    .build();
            return addCellDep(cellDep);
        }

        public Builder setHeaderDeps(List<byte[]> headerDeps) {
            this.headerDeps = headerDeps;
            return this;
        }

        public Builder addHeaderDep(byte[] headerDep) {
            this.headerDeps.add(headerDep);
            return this;
        }

        public Builder addHeaderDep(String headerDep) {
            addHeaderDep(Hex.toByteArray(headerDep));
            return this;
        }

        public Builder setInputs(List<CellInput> inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder addInput(CellInput inputs) {
            this.inputs.add(inputs);
            return this;
        }

        public Builder addInput(String txHash, int index) {
            CellInput cellInput = new CellInput(new OutPoint(txHash, index));
            return addInput(cellInput);
        }

        public Builder addInput(byte[] txHash, int index) {
            CellInput cellInput = new CellInput(new OutPoint(txHash, index));
            return addInput(cellInput);
        }

        public Builder setOutputs(List<Cell> outputs) {
            this.outputs = outputs;
            return this;
        }

        public Builder addOutput(Cell output) {
            this.outputs.add(output);
            return this;
        }

        public Builder setWitnesses(List<byte[]> witnesses) {
            this.witnesses = witnesses;
            return this;
        }

        public Builder addWitness(byte[] witness) {
            this.witnesses.add(witness);
            return this;
        }

        public Builder addWitness(String witness) {
            addWitness(Hex.toByteArray(witness));
            return this;
        }

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.setVersion(version);
            transaction.setCellDeps(cellDeps);
            transaction.setHeaderDeps(headerDeps);
            transaction.setInputs(inputs);
            transaction.setOutputs(outputs);
            transaction.setWitnesses(witnesses);
            if (witnesses.size() < inputs.size()) {
                for (int i = witnesses.size(); i < inputs.size(); i++) {
                    witnesses.add(new byte[0]);
                }
            }
            return transaction;
        }
    }

    protected static class TypeAdapter implements JsonDeserializer<Transaction> {
        @Override
        public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            Transaction tx = new Transaction();
            tx.version = context.deserialize(obj.get("version"), int.class);
            tx.cellDeps = context.deserialize(obj.get("cell_deps"), new TypeToken<List<CellDep>>() {
            }.getType());
            tx.headerDeps = context.deserialize(obj.get("header_deps"), new TypeToken<List<byte[]>>() {
            }.getType());
            tx.inputs = context.deserialize(obj.get("inputs"), new TypeToken<List<CellInput>>() {
            }.getType());
            tx.outputs = context.deserialize(obj.get("outputs"), new TypeToken<List<Cell>>() {
            }.getType());
            List<byte[]> outputsData = context.deserialize(obj.get("outputs_data"), new TypeToken<List<byte[]>>() {
            }.getType());
            for (int i = 0; i < tx.outputs.size(); i++) {
                tx.outputs.get(i).setData(outputsData.get(i));
            }
            tx.witnesses = context.deserialize(obj.get("witnesses"), new TypeToken<List<byte[]>>() {
            }.getType());
            return tx;
        }
    }
}
