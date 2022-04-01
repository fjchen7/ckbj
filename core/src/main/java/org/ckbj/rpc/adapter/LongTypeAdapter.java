package org.ckbj.rpc.adapter;

import com.google.gson.*;
import org.ckbj.utils.Hex;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class LongTypeAdapter implements JsonDeserializer<Long>, JsonSerializer<Long> {

    @Override
    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Hex.encode(BigInteger.valueOf(src).toByteArray(), true, false));
    }

    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.getAsJsonPrimitive().isNumber()) {
            return json.getAsLong();
        }
        byte[] bytes = Hex.decode(json.getAsString());
        return new BigInteger(bytes).longValue();
    }
}
