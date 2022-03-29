package org.ckbj.protocol.type;

import org.ckbj.Config;
import org.ckbj.ScriptInfo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DetailedTransaction {
    protected long version;
    protected List<CellDep> cellDeps;
    protected List<byte[]> headerDeps;
    protected List<DetailedCell> inputs;
    protected List<byte[]> witnesses;
    protected List<CellOutput> outputs;
    protected List<byte[]> outputData;

    public DetailedTransaction() {
        this.version = 0;
        this.cellDeps = new ArrayList<>();
        this.headerDeps = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.witnesses = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.outputData = new ArrayList<>();
    }

    public Transaction toTransaction() {
        return null;
    }

    public static class DetailedTransactionBuilder extends DetailedTransaction {
        private Config config;

        public DetailedTransactionBuilder() {
            super();
        }

        public DetailedTransactionBuilder(Config config) {
            super();
            this.config = config;
        }

        public DetailedTransactionBuilder addCellDep(CellDep cellDep) {
            cellDeps.add(cellDep);
            return this;
        }

        public DetailedTransactionBuilder addCellDeps(List<CellDep> cellDeps) {
            cellDeps.addAll(cellDeps);
            return this;
        }

        public DetailedTransactionBuilder addHeaderDep(byte[] headerDep) {
            headerDeps.add(headerDep);
            return this;
        }

        public DetailedTransactionBuilder addHeaderDeps(List<byte[]> headerDeps) {
            headerDeps.addAll(headerDeps);
            return this;
        }

        public DetailedTransactionBuilder addInput(DetailedCell input) {
            inputs.add(input);
            return this;
        }

        public DetailedTransactionBuilder addInputs(List<DetailedCell> inputs) {
            inputs.addAll(inputs);
            return this;
        }


        public DetailedTransactionBuilder addWitness(byte[] witness) {
            witnesses.add(witness);
            return this;
        }

        public DetailedTransactionBuilder addWitnesses(List<byte[]> witnesses) {
            witnesses.addAll(witnesses);
            return this;
        }

        public DetailedTransactionBuilder addOutput(CellOutput output, byte[] outputData) {
            this.outputs.add(output);
            this.outputData.add(outputData);
            return this;
        }

        public DetailedTransactionBuilder addOutput(String address, BigInteger capacity) {
            return addOutput(
                    new CellOutput(capacity, null, Script.fromAddress(address)),
                    new byte[]{});
        }

        public DetailedTransaction build() {
            return build(this.config);
        }

        public DetailedTransaction build(Config config) {
            for (DetailedCell input: this.inputs) {
                fillScriptInfo(input.getLockScript(), config);
                fillScriptInfo(input.getLockScript(), config);
            }
            for (CellOutput output: this.outputs) {
                fillScriptInfo(output.getLockScript(), config);
            }
            return this;
        }

        private void fillScriptInfo(Script script, Config config) {
            ScriptInfo scriptInfo = config.getScriptInfo(script);
            if (scriptInfo != null) {
                this.addCellDeps(scriptInfo.getCellDeps());
            }
        }
    }
}
