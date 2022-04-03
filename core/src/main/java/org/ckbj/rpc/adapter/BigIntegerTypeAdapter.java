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
        String hex = Hex.toHexString(src, true);
        hex = Hex.onlyKeepSignificantHex(hex);
        return new JsonPrimitive(hex);
    }

    @Override
    public BigInteger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Hex.toBigInteger(json.getAsString());
    }
}
