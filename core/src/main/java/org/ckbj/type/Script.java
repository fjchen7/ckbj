package org.ckbj.type;

import com.google.gson.annotations.SerializedName;
import org.ckbj.utils.Hash;

import java.math.BigInteger;

public final class Script {
    private byte[] codeHash;
    private byte[] args;
    private HashType hashType = HashType.TYPE;

    public Script(byte[] codeHash) {
        setCodeHash(codeHash);
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

    public Script setCodeHash(byte[] codeHash) {
        if (!Hash.isHash(codeHash)) {
            throw new IllegalArgumentException("codeHash should be 32 bytes");
        }
        this.codeHash = codeHash;
        return this;
    }

    public Script setArgs(byte[] args) {
        this.args = args;
        return this;
    }

    public Script setHashType(HashType hashType) {
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
