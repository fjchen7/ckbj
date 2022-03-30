package org.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.ckbj.utils.Hex;

import java.lang.reflect.Type;

@JsonAdapter(EpochFraction.TypeAdapter.class)
public class EpochFraction {
    private int number;
    private int blockIndex;
    private int length;

    private EpochFraction() {
    }

    public int getNumber() {
        return number;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public int getLength() {
        return length;
    }

    protected class TypeAdapter implements JsonSerializer<EpochFraction>, JsonDeserializer<EpochFraction> {

        @Override
        public JsonElement serialize(EpochFraction src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            // encode: length || index || number
            //           0  1 || 2  3  || 4  5  6
            // See: https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0019-data-structures/0019-data-structures.md#header
            byte[] bytes = new byte[7];
            encode(bytes, src.number, 4, 6);
            encode(bytes, src.blockIndex, 2, 3);
            encode(bytes, src.length, 0, 1);
            return new JsonPrimitive(Hex.encode(bytes));
        }

        @Override
        public EpochFraction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            byte[] bytes = Hex.decode(json.getAsString());
            if (bytes.length == 6) {
                byte[] copied = bytes;
                bytes = new byte[7];
                System.arraycopy(copied, 0, bytes, 1, 6);
            }
            EpochFraction epoch = new EpochFraction();
            epoch.number = decode(bytes, 4, 6);
            epoch.blockIndex = decode(bytes, 2, 3);
            epoch.length = decode(bytes, 0, 1);
            return epoch;
        }

        private int decode(byte[] bytes, int start, int end) {
            int number = 0;
            for (int i = start; i <= end; i++) {
                number = number * 256 + (bytes[i] & 0xff);
            }
            return number;
        }

        private void encode(byte[] bytes, int number, int start, int end) {
            int bit = 0;
            for (int i = end; i >= start; i--) {
                bytes[i] = (byte) (number >> bit & 0xff);
                bit += 8;
            }
        }
    }
}
