package org.ckbj.rpc.adapter;

import com.google.gson.*;
import org.ckbj.utils.Hex;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class BigIntegerTypeAdapter implements JsonSerializer<BigInteger>, JsonDeserializer<BigInteger> {

    @Override
    public JsonElement serialize(BigInteger src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        return new JsonPrimitive(Hex.encode(src.toByteArray()));
    }

    @Override
    public BigInteger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new BigInteger(1, Hex.decode(json.getAsString()));
    }
}
