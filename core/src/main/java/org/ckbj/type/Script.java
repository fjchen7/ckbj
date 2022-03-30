package org.ckbj.type;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Objects;

public final class Script {
    private byte[] codeHash;
    private byte[] args;
    private HashType hashType;

    private Script() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Script clone) {
        return new Builder(clone);
    }

    public byte[] getCodeHash() {
        return codeHash;
    }

    public byte[] getArgs() {
        return args;
    }

    public HashType getHashType() {
        return hashType;
    }

    // TODO
    public byte[] hash() {
        return null;
//    Blake2b blake2b = new Blake2b();
//    blake2b.update(Encoder.encode(Serializer.serializeScript(this)));
//    return blake2b.doFinalBytes();
    }

    // TODO
    public BigInteger occupiedCapacity() {
        return null;
//    int byteSize = 1;
//    if (!Strings.isEmpty(codeHash)) {
//      byteSize += Hex.encode(codeHash).length;
//    }
//    if (!Strings.isEmpty(args)) {
//      byteSize += Hex.encode(args).length;
//    }
//    return Utils.ckbToShannon(byteSize);
    }

    // TODO
    public String toAddress() {
        return null;
    }

    // TODO
    public static org.ckbj.protocol.type.Script fromAddress(String address) {
        return null;
    }

    public static final class Builder {
        public byte[] codeHash;
        public byte[] args;
        public HashType hashType;

        private Builder() {
            args = new byte[]{};
            hashType = HashType.TYPE;
        }

        private Builder(Script clone) {
            codeHash = clone.codeHash;
            args = clone.args;
            hashType = clone.hashType;
        }

        public Builder setCodeHash(byte[] codeHash) {
            if (codeHash.length != 32) {
                throw new IllegalArgumentException("codeHash should be 32 bytes");
            }
            this.codeHash = codeHash;
            return this;
        }

        public Builder setArgs(byte[] args) {
            Objects.requireNonNull(args);
            this.args = args;
            return this;
        }

        public Builder setHashType(HashType hashType) {
            Objects.requireNonNull(hashType);
            this.hashType = hashType;
            return this;
        }

        public Script build() {
            Script script = new Script();
            script.codeHash = this.codeHash;
            script.hashType = this.hashType;
            script.args = this.args;
            return script;
        }
    }

    public enum HashType {
        @SerializedName("data")
        DATA(0x00),
        @SerializedName("type")
        TYPE(0x01),
        @SerializedName("data1")
        DATA1(0x02);

        private byte value;

        HashType(int value) {
            this.value = (byte) value;
        }

        public byte toByte() {
            return value;
        }
    }

}
