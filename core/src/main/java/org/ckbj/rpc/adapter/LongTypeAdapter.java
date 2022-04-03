package org.ckbj.rpc.adapter;

import com.google.gson.*;
import org.ckbj.utils.Hex;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class LongTypeAdapter implements JsonDeserializer<Long>, JsonSerializer<Long> {

    @Override
    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Hex.toHexString(BigInteger.valueOf(src), true, false));
    }

    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.getAsJsonPrimitive().isNumber()) {
            return json.getAsLong();
        }
        BigInteger value = Hex.hexStringToBigInteger(json.getAsString());
        return value.longValue();
    }
}
