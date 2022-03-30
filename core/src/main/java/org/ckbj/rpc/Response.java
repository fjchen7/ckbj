package org.ckbj.rpc;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.ckbj.rpc.exception.JsonRpcException;

import java.lang.reflect.Type;

public class Response<T> {
    private long id;
    private String jsonrpc;
    private T result;
    private Error error;

    public long getId() {
        return id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public T getResult() {
        return result;
    }

    public T getResultOrThrowException() {
        if (hasError()) {
            throw JsonRpcException.newException(getError());
        }
        return getResult();
    }

    public Error getError() {
        return error;
    }

    public boolean hasError() {
        return error != null;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @JsonAdapter(Error.TypeAdapter.class)
    public static class Error {
        private int code;
        private String message;

        public Error() {
        }

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        protected class TypeAdapter implements JsonDeserializer<Error> {
            @Override
            public Error deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject obj = json.getAsJsonObject();
                Error error = new Error();
                error.setMessage(obj.get("message").getAsString());
                error.setCode(obj.get("code").getAsInt());
                return error;
            }
        }
    }
}
