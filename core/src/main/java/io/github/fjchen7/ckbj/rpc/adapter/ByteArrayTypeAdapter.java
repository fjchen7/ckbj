package io.github.fjchen7.ckbj.rpc.adapter;

import com.google.gson.*;
import io.github.fjchen7.ckbj.utils.Hex;

import java.lang.reflect.Type;

public class ByteArrayTypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {

    @Override
    public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        return new JsonPrimitive(Hex.toHexString(src));
    }

    @Override
    public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Hex.toByteArray(json.getAsString());
    }
}
