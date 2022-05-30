package org.ckbj.type;

import org.ckbj.chain.Contract;
import org.ckbj.chain.ContractArgs;
import org.ckbj.chain.ContractCollection;
import org.ckbj.chain.address.Address;
import org.ckbj.utils.Capacity;
import org.ckbj.utils.Hex;

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

    public SmartTransactionBuilder addCellDeps(Contract.Type contractTypes) {
        return addCellDeps(contractCollection.getContract(contractTypes));
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

    public SmartTransactionBuilder addInput(CellInput input) {
        builder.addInput(input);
        return this;
    }

    public SmartTransactionBuilder addInput(byte[] txHash, int index) {
        CellInput cellInput = new CellInput(new OutPoint(txHash, index));
        return addInput(cellInput);
    }

    public SmartTransactionBuilder addInput(String txHash, int index) {
        return addInput(Hex.toByteArray(txHash), index);
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

    public SmartTransactionBuilder addWitness() {
        return addWitness(new byte[0]);
    }

    public SmartTransactionBuilder addWitness(byte[] witness) {
        builder.addWitness(witness);
        return this;
    }

    public SmartTransactionBuilder addWitness(String witness) {
        return addWitness(Hex.toByteArray(witness));
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

        public OutputBuilder setType(Contract.Type contractType, byte[] args) {
            if (contractType.getScriptType() != Script.Type.TYPE) {
                throw new IllegalArgumentException("contractType must be TYPE");
            }
            Script type = contractCollection.getContract(contractType)
                    .createScript(args);
            return setType(type);
        }

        public OutputBuilder setType(Contract.Type contractType, ContractArgs args) {
            return setType(contractType, args.getArgs());
        }

        public OutputBuilder setType(Contract.Type contractType, String args) {
            return setType(contractType, Hex.toByteArray(args));
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

        public OutputBuilder setLock(Contract.Type contractType, byte[] args) {
            if (contractType.getScriptType() != Script.Type.LOCK) {
                throw new IllegalArgumentException("contractType must be LOCK");
            }
            Script lock = contractCollection.getContract(contractType)
                    .createScript(args);
            return setLock(lock);
        }

        public OutputBuilder setLock(Contract.Type contractType, ContractArgs args) {
            return setLock(contractType, args.getArgs());
        }

        public OutputBuilder setLock(Contract.Type contractType, String args) {
            return setLock(contractType, Hex.toByteArray(args));
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