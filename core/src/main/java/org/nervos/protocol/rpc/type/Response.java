package org.nervos.protocol.rpc.type;

public class Response<T> {
    private long id;
    private String jsonrpc;
    private T result;
    private Error error;
    private String rawResponse;


    public long getId() {
        return id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public T getResult() {
        return result;
    }

    public Error getError() {
        return error;
    }

    public String getRawResponse() {
        return rawResponse;
    }
}
