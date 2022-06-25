package io.github.fjchen7.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import io.github.fjchen7.ckbj.utils.Hex;

import java.lang.reflect.Type;

@JsonAdapter(EpochFraction.TypeAdapter.class)
public class EpochFraction {
    private int number;
    private int blockIndex;
    private int length;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] toByteArray() {
        return EpochFraction.encode(this);
    }

    public static byte[] encode(EpochFraction src) {
        // encode: length || index || number
        //           0  1 || 2  3  || 4  5  6
        // See: https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0019-data-structures/0019-data-structures.md#header
        byte[] bytes = new byte[7];
        encode(bytes, src.number, 4, 6);
        encode(bytes, src.blockIndex, 2, 3);
        encode(bytes, src.length, 0, 1);
        return bytes;
    }

    public static EpochFraction decode(byte[] src) {
        if (src.length < 7) {
            byte[] dest = new byte[7];
            System.arraycopy(src, 0, dest, 7 - src.length, src.length);
            src = dest;
        }
        EpochFraction epoch = new EpochFraction();
        epoch.number = decode(src, 4, 6);
        epoch.blockIndex = decode(src, 2, 3);
        epoch.length = decode(src, 0, 1);
        return epoch;
    }

    private static void encode(byte[] bytes, int number, int start, int end) {
        int bit = 0;
        for (int i = end; i >= start; i--) {
            bytes[i] = (byte) (number >> bit & 0xff);
            bit += 8;
        }
    }

    private static int decode(byte[] bytes, int start, int end) {
        int number = 0;
        for (int i = start; i <= end; i++) {
            number = number * 256 + (bytes[i] & 0xff);
        }
        return number;
    }

    protected class TypeAdapter implements JsonSerializer<EpochFraction>, JsonDeserializer<EpochFraction> {
        @Override
        public JsonElement serialize(EpochFraction src, Type typeOfSrc, JsonSerializationContext context) {
            byte[] bytes = EpochFraction.encode(src);
            return new JsonPrimitive(Hex.toHexString(bytes));
        }

        @Override
        public EpochFraction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            byte[] bytes = Hex.toByteArray(json.getAsString());
            return EpochFraction.decode(bytes);
        }
    }
}
