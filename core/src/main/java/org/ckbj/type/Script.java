package org.ckbj.type;

import com.google.gson.annotations.SerializedName;
import org.ckbj.crypto.Blake2b;
import org.ckbj.molecule.Serializer;
import org.ckbj.utils.Hex;

import java.util.Arrays;

public final class Script {
    private byte[] codeHash;
    private byte[] args = new byte[0];
    private HashType hashType = HashType.TYPE;

    public static Builder builder() {
        return new Builder();
    }

    public byte[] getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(byte[] codeHash) {
        if (codeHash.length != 32) {
            throw new IllegalArgumentException("codeHash should be 32 bytes");
        }
        this.codeHash = codeHash;
    }

    public void setCodeHash(String codeHash) {
        setCodeHash(Hex.toByteArray(codeHash));
    }

    public byte[] getArgs() {
        return args;
    }

    public void setArgs(byte[] args) {
        this.args = args;
    }

    public void setArgs(String args) {
        setArgs(Hex.toByteArray(args));
    }

    public HashType getHashType() {
        return hashType;
    }

    public void setHashType(HashType hashType) {
        this.hashType = hashType;
    }

    public byte[] hash() {
        byte[] serialization = Serializer.serialize(this);
        return Blake2b.digest256(serialization);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Script script = (Script) o;

        if (!Arrays.equals(codeHash, script.codeHash)) return false;
        if (!Arrays.equals(args, script.args)) return false;
        return hashType == script.hashType;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(codeHash);
        result = 31 * result + Arrays.hashCode(args);
        result = 31 * result + hashType.hashCode();
        return result;
    }

    public static final class Builder {
        private byte[] codeHash;
        private byte[] args = new byte[0];
        private HashType hashType = HashType.TYPE;

        private Builder() {
        }

        public Builder(Script script) {
            this.codeHash = script.codeHash;
            this.args = script.args;
            this.hashType = script.hashType;
        }

        public Builder setCodeHash(byte[] codeHash) {
            this.codeHash = codeHash;
            return this;
        }

        public Builder setCodeHash(String codeHash) {
            return setCodeHash(Hex.toByteArray(codeHash));
        }

        public Builder setArgs(byte[] args) {
            this.args = args;
            return this;
        }

        public Builder setArgs(String args) {
            return setArgs(Hex.toByteArray(args));
        }

        public Builder setHashType(HashType hashType) {
            this.hashType = hashType;
            return this;
        }

        public Script build() {
            Script script = new Script();
            script.setCodeHash(codeHash);
            script.setArgs(args);
            script.setHashType(hashType);
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

        private byte byteValue;

        HashType(int byteValue) {
            this.byteValue = (byte) byteValue;
        }

        public byte toByte() {
            return byteValue;
        }

        public static HashType valueOf(byte value) {
            switch (value) {
                case 0x00:
                    return DATA;
                case 0x01:
                    return TYPE;
                case 0x02:
                    return DATA1;
                default:
                    throw new NullPointerException();
            }
        }
    }
}
