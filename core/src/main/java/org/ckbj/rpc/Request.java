package org.ckbj.rpc;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@JsonAdapter(Request.TypeAdapter.class)
public class Request<T extends Response> {
    private static AtomicLong nextId = new AtomicLong(0);
    private String jsonrpc = "2.0";
    private String method;
    private List params;
    private long id;

    private transient Class<T> responseType;

    public Request(String method, List params, Class<T> responseType) {
        this.method = method;
        this.params = params;
        this.id = nextId.getAndIncrement();
        this.responseType = responseType;
    }

    public Request(String method, Class<T> responseType) {
        this(method, Collections.emptyList(), responseType);
    }

    public T send(JsonRpcService client) throws IOException {
        return client.send(this, responseType);
    }

    protected class TypeAdapter implements JsonSerializer<Request<T>> {
        @Override
        public JsonElement serialize(Request<T> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.add("jsonrpc", context.serialize(src.jsonrpc));
            obj.add("method", context.serialize(src.method));
            obj.add("params", context.serialize(src.params));
            obj.add("id", new JsonPrimitive(src.id));
            return obj;
        }
    }
}
