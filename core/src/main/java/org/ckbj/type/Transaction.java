package org.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import org.ckbj.chain.Contract;
import org.ckbj.chain.ContractCollection;
import org.ckbj.chain.address.Address;
import org.ckbj.crypto.Blake2b;
import org.ckbj.molecule.Serializer;
import org.ckbj.utils.Capacity;
import org.ckbj.utils.Hex;

import java.lang.reflect.Type;
import java.util.*;

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

    public static Builder builder(ContractCollection contractCollection) {
        return new Builder(contractCollection);
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
        private ContractCollection contractCollection;

        private int version = 0;
        private List<CellDep> cellDeps = new ArrayList<>();
        private List<byte[]> headerDeps = new ArrayList<>();
        private List<CellInput> inputs = new ArrayList<>();
        private List<Cell> outputs = new ArrayList<>();
        private List<byte[]> witnesses = new ArrayList<>();

        public Builder(ContractCollection contractCollection) {
            this.contractCollection = contractCollection;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder setCellDep(List<CellDep> cellDeps) {
            Objects.requireNonNull(cellDeps);
            this.cellDeps = cellDeps;
            return this;
        }

        public Builder addCellDep(CellDep cellDep) {
            Objects.requireNonNull(cellDep);
            this.cellDeps.add(cellDep);
            return this;
        }

        public Builder addCellDep(Collection<CellDep> cellDeps) {
            for (CellDep cellDep: cellDeps) {
                addCellDep(cellDep);
            }
            return this;
        }

        public Builder addCellDep(Contract contract) {
            return addCellDep(contract.getCellDeps());
        }

        public Builder addCellDep(Contract.Type contractTypes) {
            return addCellDep(contractCollection.getContract(contractTypes));
        }

        public Builder addCellDep(Script script) {
            return addCellDep(contractCollection.getContract(script).getCellDeps());
        }

        public Builder addCellDep(CellDep.DepType depType, String txHash, int index) {
            return addCellDep(depType, Hex.toByteArray(txHash), index);
        }

        public Builder addCellDep(CellDep.DepType depType, byte[] txHash, int index) {
            Objects.requireNonNull(depType);
            CellDep cellDep = CellDep.builder()
                    .setDepType(depType)
                    .setOutPoint(new OutPoint(txHash, index))
                    .build();
            return addCellDep(cellDep);
        }

        public Builder setHeaderDeps(List<byte[]> headerDeps) {
            Objects.requireNonNull(headerDeps);
            this.headerDeps = headerDeps;
            return this;
        }

        public Builder addHeaderDep(byte[] headerDep) {
            if (headerDep.length != 32) {
                throw new IllegalArgumentException("headerDep length must be 32");
            }
            this.headerDeps.add(headerDep);
            return this;
        }

        public Builder addHeaderDep(Collection<byte[]> headerDeps) {
            for (byte[] headerDep: headerDeps) {
                addHeaderDep(headerDep);
            }
            return this;
        }

        public Builder setInputs(List<CellInput> inputs) {
            Objects.requireNonNull(inputs);
            this.inputs = inputs;
            return this;
        }

        public Builder addInput(CellInput inputs) {
            Objects.requireNonNull(inputs);
            this.inputs.add(inputs);
            return this;
        }

        public Builder addInput(Collection<CellInput> inputs) {
            for (CellInput input: inputs) {
                addInput(input);
            }
            return this;
        }

        public Builder addInput(byte[] txHash, int index) {
            CellInput cellInput = new CellInput(new OutPoint(txHash, index));
            return addInput(cellInput);
        }

        public Builder addInput(String txHash, int index) {
            return addInput(Hex.toByteArray(txHash), index);
        }

        public Builder setOutputs(List<Cell> outputs) {
            Objects.requireNonNull(outputs);
            this.outputs = outputs;
            return this;
        }

        public Builder addOutput(Cell output) {
            Objects.requireNonNull(output);
            this.outputs.add(output);
            if (output.getType() != null) {
                addCellDep(contractCollection.getContract(output.getType()));
            }
            return this;
        }

        public Builder addOutput(Collection<Cell> outputs) {
            for (Cell output: outputs) {
                this.outputs.add(output);
            }
            return this;
        }

        public Builder addOutput(Address address, long shannon) {
            if (address.getNetwork() != contractCollection) {
                throw new IllegalArgumentException("Address network is not " + contractCollection);
            }
            Cell cell = Cell.builder()
                    .setLock(address.getScript())
                    .setCapacity(shannon)
                    .build();
            return addOutput(cell);
        }

        public Builder addOutput(String address, long capacity) {
            return addOutput(Address.decode(address), capacity);
        }

        public Builder addOutputInBytes(Address address, double bytes) {
            return addOutput(address, Capacity.bytesToShannon(bytes));
        }

        public Builder addOutputInBytes(String address, double bytes) {
            return addOutput(address, Capacity.bytesToShannon(bytes));
        }

        public Builder setWitnesses(List<byte[]> witnesses) {
            Objects.requireNonNull(witnesses);
            this.witnesses = witnesses;
            return this;
        }

        public Builder addWitness(byte[] witness) {
            Objects.requireNonNull(witness);
            this.witnesses.add(witness);
            return this;
        }

        public Builder addWitness(String witness) {
            return addWitness(Hex.toByteArray(witness));
        }

        public Transaction build() {
            for (int i = witnesses.size(); i < inputs.size(); i++) {
                witnesses.add(new byte[0]);
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
            Set<CellDep> unique = new HashSet<>();
            List<CellDep> filter = new ArrayList<>();
            for (CellDep cellDep: this.cellDeps) {
                if (!unique.contains(cellDep)) {
                    unique.add(cellDep);
                    filter.add(cellDep);
                }
            }
            this.cellDeps = filter;
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
