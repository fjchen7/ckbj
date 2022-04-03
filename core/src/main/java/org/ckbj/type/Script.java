package org.ckbj.type;

import com.google.gson.annotations.SerializedName;
import org.ckbj.utils.Hash;
import org.ckbj.utils.Hex;

import java.util.Arrays;
import java.util.Objects;

public final class Script {
    private byte[] codeHash;
    private byte[] args = new byte[0];
    private HashType hashType = HashType.TYPE;

    public byte[] getCodeHash() {
        return codeHash;
    }

    public byte[] getArgs() {
        return args;
    }

    public HashType getHashType() {
        return hashType;
    }

    public Script setCodeHash(String codeHash) {
        return setCodeHash(Hex.decode(codeHash));
    }

    public Script setCodeHash(byte[] codeHash) {
        Objects.requireNonNull(codeHash);
        if (!Hash.isHash(codeHash)) {
            throw new IllegalArgumentException("codeHash should be 32 bytes");
        }
        this.codeHash = codeHash;
        return this;
    }

    public Script setArgs(String args) {
        return setArgs(Hex.decode(args));
    }

    public Script setArgs(byte[] args) {
        Objects.requireNonNull(args);
        this.args = args;
        return this;
    }

    public Script setHashType(HashType hashType) {
        Objects.requireNonNull(hashType);
        this.hashType = hashType;
        return this;
    }

    // TODO
    public byte[] hash() {
        return null;
//    Blake2b blake2b = new Blake2b();
//    blake2b.update(Encoder.encode(Serializer.serializeScript(this)));
//    return blake2b.doFinalBytes();
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
