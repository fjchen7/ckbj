package org.ckbj.type;

import java.util.Arrays;

public class WitnessArgs {
    // Lock args
    private byte[] lock;
    // Type args for input
    private byte[] inputType;
    // Type args for output
    private byte[] outputType;

    public byte[] getLock() {
        return lock;
    }

    public WitnessArgs setLock(byte[] lock) {
        this.lock = lock;
        return this;
    }

    public byte[] getInputType() {
        return inputType;
    }

    public WitnessArgs setInputType(byte[] inputType) {
        this.inputType = inputType;
        return this;
    }

    public byte[] getOutputType() {
        return outputType;
    }

    public WitnessArgs setOutputType(byte[] outputType) {
        this.outputType = outputType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessArgs that = (WitnessArgs) o;

        if (!Arrays.equals(lock, that.lock)) return false;
        if (!Arrays.equals(inputType, that.inputType)) return false;
        return Arrays.equals(outputType, that.outputType);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(lock);
        result = 31 * result + Arrays.hashCode(inputType);
        result = 31 * result + Arrays.hashCode(outputType);
        return result;
    }
}
