package org.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import org.ckbj.crypto.Blake2b;
import org.ckbj.molecule.Serializer;
import org.ckbj.utils.Capacity;
import org.ckbj.utils.Hex;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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

    public CellDep getCellDep(int i) {
        return cellDeps.get(i);
    }

    public void setCellDeps(List<CellDep> cellDeps) {
        this.cellDeps = cellDeps;
    }

    public List<byte[]> getHeaderDeps() {
        return headerDeps;
    }

    public byte[] getHeaderDep(int i) {
        return headerDeps.get(i);
    }

    public void setHeaderDeps(List<byte[]> headerDeps) {
        this.headerDeps = headerDeps;
    }

    public List<CellInput> getInputs() {
        return inputs;
    }

    public CellInput getInput(int i) {
        return inputs.get(i);
    }

    public void setInputs(List<CellInput> inputs) {
        this.inputs = inputs;
    }

    public List<Cell> getOutputs() {
        return outputs;
    }

    public Cell getOutput(int i) {
        return outputs.get(i);
    }

    public void setOutputs(List<Cell> outputs) {
        this.outputs = outputs;
    }

    public List<byte[]> getOutputsData() {
        List<byte[]> outputsData = new ArrayList<>();
        for (Cell output: outputs) {
            outputsData.add(output.getData());
        }
        return outputsData;
    }

    public byte[] getOutputData(int i) {
        return outputs.get(i).getData();
    }

    public List<byte[]> getWitnesses() {
        return witnesses;
    }

    public byte[] getWitness(int i) {
        return witnesses.get(i);
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

        public Builder addCellDeps(List<CellDep> cellDeps) {
            this.cellDeps.addAll(cellDeps);
            return this;
        }

        public Builder addCellDeps(CellDep... cellDeps) {
            this.cellDeps.addAll(Arrays.asList(cellDeps));
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
            return addCellDeps(cellDep);
        }

        public Builder setHeaderDeps(List<byte[]> headerDeps) {
            this.headerDeps = headerDeps;
            return this;
        }

        public Builder addHeaderDeps(byte[]... headerDeps) {
            this.headerDeps.addAll(Arrays.asList(headerDeps));
            return this;
        }

        public Builder addHeaderDeps(String... headerDep) {
            for (String header: headerDep) {
                this.headerDeps.add(Hex.toByteArray(header));
            }
            return this;
        }

        public Builder setInputs(List<CellInput> inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder addInputs(CellInput... inputs) {
            this.inputs.addAll(Arrays.asList(inputs));
            return this;
        }

        public Builder addInput(String txHash, int index) {
            CellInput cellInput = new CellInput(new OutPoint(txHash, index));
            return addInputs(cellInput);
        }

        public Builder addInput(byte[] txHash, int index) {
            CellInput cellInput = new CellInput(new OutPoint(txHash, index));
            return addInputs(cellInput);
        }

        public Builder setOutputs(List<Cell> outputs) {
            this.outputs = new ArrayList<>();
            for (Cell output: outputs) {
                addOutput(output);
            }
            return this;
        }

        public Builder addOutput(Cell output) {
            long capacity = output.getCapacity();
            long occupation = Capacity.occupation(output, false);
            if (capacity < occupation) {
                throw new IllegalArgumentException("capacity " + capacity + " is less than output occupation " + occupation);
            } else {
                this.outputs.add(output);
            }
            return this;
        }

        public Builder addOutputs(Cell... outputs) {
            for (Cell output: outputs) {
                addOutput(output);
            }
            return this;
        }

        public Builder addOutput(Script lockScript, long capacity) {
            Cell cell = Cell.builder()
                    .setCapacity(capacity)
                    .setLock(lockScript)
                    .build();
            return addOutputs(cell);
        }

        public Builder addOutput(Script lockScript, double capacityInBytes) {
            return addOutput(lockScript, Capacity.bytesToShannon(capacityInBytes));
        }

        public Builder addOutput(Address address, long capacity) {
            return addOutput(address.getScript(), capacity);
        }

        public Builder addOutput(Address address, double capacityInBytes) {
            return addOutput(address.getScript(), Capacity.bytesToShannon(capacityInBytes));
        }

        public Builder addOutput(String address, long capacity) {
            return addOutput(Address.decode(address), capacity);
        }

        public Builder addOutput(String address, double capacityInBytes) {
            return addOutput(Address.decode(address), Capacity.bytesToShannon(capacityInBytes));
        }

        public Builder setWitnesses(List<byte[]> witnesses) {
            this.witnesses = witnesses;
            return this;
        }

        public Builder addWitnesses(byte[]... witnesses) {
            for (byte[] witness: witnesses) {
                this.witnesses.add(witness);
            }
            return this;
        }

        public Builder addWitnesses(String... witnesses) {
            for (String witness: witnesses) {
                addWitnesses(Hex.toByteArray(witness));
            }
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

    protected static class TypeAdapter implements JsonDeserializer<Transaction>, JsonSerializer<Transaction> {
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

        @Override
        public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.add("version", context.serialize(src.version));
            obj.add("cell_deps", context.serialize(src.cellDeps));
            obj.add("header_deps", context.serialize(src.headerDeps));
            obj.add("inputs", context.serialize(src.inputs));
            JsonArray outputs = new JsonArray();
            JsonArray outputsData = new JsonArray();
            for (int i = 0; i < src.outputs.size(); i++) {
                JsonObject output = new JsonObject();
                output.add("capacity", context.serialize(src.outputs.get(i).getCapacity()));
                output.add("lock", context.serialize(src.outputs.get(i).getLock()));
                output.add("type", context.serialize(src.outputs.get(i).getType()));
                outputs.add(output);
                outputsData.add(context.serialize(src.outputs.get(i).getData()));
            }
            obj.add("outputs", outputs);
            obj.add("outputs_data", context.serialize(outputsData));
            obj.add("witnesses", context.serialize(src.witnesses));
            return obj;
        }
    }
}
