package io.github.fjchen7.ckbj.type;

import io.github.fjchen7.ckbj.utils.Hex;

public final class WitnessArgsBuilder {
    private byte[] lock;
    private byte[] inputType;
    private byte[] outputType;

    WitnessArgsBuilder() {
    }

    public WitnessArgsBuilder setLock(byte[] lock) {
        this.lock = lock;
        return this;
    }

    public WitnessArgsBuilder setLock(String lock) {
        this.lock = Hex.toByteArray(lock);
        return this;
    }

    public WitnessArgsBuilder setInputType(byte[] inputType) {
        this.inputType = inputType;
        return this;
    }

    public WitnessArgsBuilder setInputType(String inputType) {
        this.inputType = Hex.toByteArray(inputType);
        return this;
    }

    public WitnessArgsBuilder setOutputType(byte[] outputType) {
        this.outputType = outputType;
        return this;
    }

    public WitnessArgsBuilder setOutputType(String outputType) {
        this.outputType = Hex.toByteArray(outputType);
        return this;
    }

    public WitnessArgs build() {
        WitnessArgs witnessArgs = new WitnessArgs();
        witnessArgs.setLock(lock);
        witnessArgs.setInputType(inputType);
        witnessArgs.setOutputType(outputType);
        return witnessArgs;
    }
}
