package org.ckbj.type;

import org.ckbj.utils.Hex;

import java.util.Arrays;

public class WitnessArgs {
    // Lock args
    private byte[] lock;
    // Type args for input
    private byte[] inputType;
    // Type args for output
    private byte[] outputType;

    public static Builder builder() {
        return new Builder();
    }

    public byte[] getLock() {
        return lock;
    }

    public void setLock(byte[] lock) {
        this.lock = lock;
    }

    public void setLock(String lock) {
        this.lock = Hex.toByteArray(lock);
    }

    public byte[] getInputType() {
        return inputType;
    }

    public void setInputType(byte[] inputType) {
        this.inputType = inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = Hex.toByteArray(inputType);
    }

    public byte[] getOutputType() {
        return outputType;
    }

    public void setOutputType(byte[] outputType) {
        this.outputType = outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = Hex.toByteArray(outputType);
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

    public static final class Builder {
        private byte[] lock;
        private byte[] inputType;
        private byte[] outputType;

        private Builder() {
        }

        public Builder setLock(byte[] lock) {
            this.lock = lock;
            return this;
        }

        public Builder setLock(String lock) {
            this.lock = Hex.toByteArray(lock);
            return this;
        }

        public Builder setInputType(byte[] inputType) {
            this.inputType = inputType;
            return this;
        }

        public Builder setInputType(String inputType) {
            this.inputType = Hex.toByteArray(inputType);
            return this;
        }

        public Builder setOutputType(byte[] outputType) {
            this.outputType = outputType;
            return this;
        }

        public Builder setOutputType(String outputType) {
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
}
