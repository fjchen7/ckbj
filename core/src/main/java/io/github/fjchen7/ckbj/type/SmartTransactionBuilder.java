package io.github.fjchen7.ckbj.type;

import io.github.fjchen7.ckbj.chain.Contract;
import io.github.fjchen7.ckbj.chain.ContractCollection;
import io.github.fjchen7.ckbj.chain.ScriptArgs;
import io.github.fjchen7.ckbj.chain.address.Address;
import io.github.fjchen7.ckbj.utils.Capacity;
import io.github.fjchen7.ckbj.utils.Hex;

import java.util.List;

public final class SmartTransactionBuilder {
    private ContractCollection contractCollection;
    private TransactionBuilder builder = new TransactionBuilder();

    SmartTransactionBuilder(ContractCollection contractCollection) {
        this.contractCollection = contractCollection;
    }

    public SmartTransactionBuilder setVersion(int version) {
        builder.setVersion(version);
        return this;
    }

    public SmartTransactionBuilder setCellDeps(List<CellDep> cellDeps) {
        builder.setCellDeps(cellDeps);
        return this;
    }

    public SmartTransactionBuilder addCellDep(CellDep cellDep) {
        builder.addCellDep(cellDep);
        return this;
    }

    public SmartTransactionBuilder addCellDep(CellDep.DepType depType, String txHash, int index) {
        return addCellDep(depType, Hex.toByteArray(txHash), index);
    }

    public SmartTransactionBuilder addCellDep(CellDep.DepType depType, byte[] txHash, int index) {
        builder.addCellDep(depType, txHash, index);
        return this;
    }

    public SmartTransactionBuilder addCellDeps(Contract contract) {
        for (CellDep cellDep: contract.getCellDeps()) {
            addCellDep(cellDep);
        }
        return this;
    }

    public SmartTransactionBuilder addCellDeps(Contract.Name contractName) {
        return addCellDeps(contractCollection.getContract(contractName));
    }

    public SmartTransactionBuilder addCellDeps(Script script) {
        return addCellDeps(contractCollection.getContract(script));
    }

    public SmartTransactionBuilder addCellDeps(Cell inputCell) {
        if (inputCell.getLock() != null) {
            addCellDeps(inputCell.getLock());
        }
        if (inputCell.getType() != null) {
            addCellDeps(inputCell.getType());
        }
        return this;
    }

    public SmartTransactionBuilder setHeaderDeps(List<byte[]> headerDeps) {
        builder.setHeaderDeps(headerDeps);
        return this;
    }

    public SmartTransactionBuilder addHeaderDep(byte[] headerHash) {
        builder.addHeaderDep(headerHash);
        return this;
    }

    public SmartTransactionBuilder addHeaderDep(String headerHash) {
        builder.addHeaderDep(Hex.toByteArray(headerHash));
        return this;
    }

    public SmartTransactionBuilder setInputs(List<CellInput> inputs) {
        builder.setInputs(inputs);
        return this;
    }

    public SmartTransactionBuilder addInput(CellInput input, byte[] witness) {
        builder.addInput(input);
        builder.addWitness(witness);
        return this;
    }

    public SmartTransactionBuilder addInput(CellInput input, String witness) {
        return addInput(input, Hex.toByteArray(witness));
    }

    public SmartTransactionBuilder addInput(CellInput input) {
        return addInput(input, new byte[0]);
    }

    public SmartTransactionBuilder addInput(byte[] txHash, int index, byte[] witness) {
        CellInput cellInput = new CellInput(new OutPoint(txHash, index));
        return addInput(cellInput, witness);
    }

    public SmartTransactionBuilder addInput(byte[] txHash, int index) {
        return addInput(txHash, index, new byte[0]);
    }

    public SmartTransactionBuilder addInput(String txHash, int index) {
        return addInput(Hex.toByteArray(txHash), index);
    }

    public SmartTransactionBuilder addInput(String txHash, int index, String witness) {
        return addInput(Hex.toByteArray(txHash), index, Hex.toByteArray(witness));
    }

    public SmartTransactionBuilder setOutputs(List<Cell> outputs) {
        builder.setOutputs(outputs);
        for (Cell output: outputs) {
            if (output.getType() != null) {
                addCellDeps(contractCollection.getContract(output.getType()));
            }
        }
        return this;
    }

    public SmartTransactionBuilder addOutput(Cell output) {
        if (output.getType() != null) {
            addCellDeps(contractCollection.getContract(output.getType()));
        }
        builder.addOutput(output);
        return this;
    }

    public SmartTransactionBuilder addOutputInShannon(Address address, long capacityInShannon) {
        Cell cell = new Cell();
        cell.setCapacity(capacityInShannon);
        cell.setLock(address.getScript());
        return addOutput(cell);
    }

    public SmartTransactionBuilder addOutputInShannon(String address, long capacityInShannon) {
        return addOutputInShannon(Address.decode(address), capacityInShannon);
    }

    public SmartTransactionBuilder addOutputInBytes(Address address, double bytes) {
        return addOutputInShannon(address, Capacity.bytesToShannon(bytes));
    }

    public SmartTransactionBuilder addOutputInBytes(String address, double bytes) {
        return addOutputInShannon(address, Capacity.bytesToShannon(bytes));
    }

    public OutputBuilder beginAddOutput() {
        return new OutputBuilder(this);
    }

    public Transaction build() {
        return builder.build();
    }

    public static class OutputBuilder {
        private SmartTransactionBuilder txBuilder;
        private ContractCollection contractCollection;

        private long capacity = -1;
        private Script type;
        private Script lock;
        private byte[] data = new byte[0];

        public OutputBuilder(SmartTransactionBuilder txBuilder) {
            this.txBuilder = txBuilder;
            contractCollection = txBuilder.contractCollection;
        }

        public OutputBuilder setCapacityInShannon(long capacityInShannon) {
            if (capacityInShannon < 0) {
                throw new IllegalArgumentException("capacity must be positive");
            }
            this.capacity = capacityInShannon;
            return this;
        }

        public OutputBuilder setCapacityInBytes(double capacityInBytes) {
            if (capacityInBytes < 0) {
                throw new IllegalArgumentException("capacity must be positive");
            }
            this.capacity = Capacity.bytesToShannon(capacityInBytes);
            return this;
        }

        public OutputBuilder setType(Script type) {
            this.type = type;
            return this;
        }

        public OutputBuilder setType(byte[] codeHash, byte[] args, Script.HashType hashType) {
            Script type = new Script();
            type.setCodeHash(args);
            type.setArgs(args);
            type.setHashType(hashType);
            return setType(type);
        }

        public OutputBuilder setType(String codeHash, String args, Script.HashType hashType) {
            return setType(Hex.toByteArray(codeHash), Hex.toByteArray(args), hashType);
        }

        public OutputBuilder setType(Contract.Name contractName, byte[] args) {
            if (contractName.getScriptType() != Script.Type.TYPE) {
                throw new IllegalArgumentException("contractName must be TYPE");
            }
            Script type = contractCollection.getContract(contractName)
                    .createScript(args);
            return setType(type);
        }

        public OutputBuilder setType(Contract.Name contractName, ScriptArgs args) {
            return setType(contractName, args.getArgs());
        }

        public OutputBuilder setType(Contract.Name contractName, String args) {
            return setType(contractName, Hex.toByteArray(args));
        }

        public OutputBuilder setLock(Script lock) {
            this.lock = lock;
            return this;
        }

        public OutputBuilder setLock(byte[] codeHash, byte[] args, Script.HashType hashType) {
            Script lock = new Script();
            lock.setCodeHash(args);
            lock.setArgs(args);
            lock.setHashType(hashType);
            return setLock(lock);
        }

        public OutputBuilder setLock(String codeHash, String args, Script.HashType hashType) {
            return setLock(Hex.toByteArray(codeHash), Hex.toByteArray(args), hashType);
        }

        public OutputBuilder setLock(Address address) {
            this.lock = address.getScript();
            return this;
        }

        public OutputBuilder setLock(String address) {
            return setLock(Address.decode(address));
        }

        public OutputBuilder setLock(Contract.Name contractName, byte[] args) {
            if (contractName.getScriptType() != Script.Type.LOCK) {
                throw new IllegalArgumentException("contractName must be LOCK");
            }
            Script lock = contractCollection.getContract(contractName)
                    .createScript(args);
            return setLock(lock);
        }

        public OutputBuilder setLock(Contract.Name contractName, ScriptArgs args) {
            return setLock(contractName, args.getArgs());
        }

        public OutputBuilder setLock(Contract.Name contractName, String args) {
            return setLock(contractName, Hex.toByteArray(args));
        }

        public OutputBuilder setData(byte[] data) {
            this.data = data;
            return this;
        }

        public OutputBuilder setData(String data) {
            this.data = Hex.toByteArray(data);
            return this;
        }

        public SmartTransactionBuilder endAddOutput() {
            if (capacity == -1) {
                throw new IllegalArgumentException("Not set capacity");
            }
            if (lock == null) {
                throw new IllegalArgumentException("Not set lock");
            }
            Cell cell = new Cell();
            cell.setCapacity(capacity);
            cell.setType(type);
            cell.setLock(lock);
            cell.setData(data);
            txBuilder.addOutput(cell);
            return txBuilder;
        }
    }
}