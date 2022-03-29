package org.ckbj.rpc;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Request<T extends Response> {
    private static AtomicLong nextId = new AtomicLong(0);
    private String jsonrpc = "2.0";
    private String method;
    private List params;
    private long id;

    private JsonRpcClient rpcClient;
    private Class<T> responseType;

    public Request(String method, List params, Class<T> responseType) {
        this.method = method;
        this.params = params;
        this.id = nextId.getAndIncrement();
        this.responseType = responseType;
    }

    public Request(String method, Class<T> responseType) {
        this(method, Collections.emptyList(), responseType);
    }

    public T send(JsonRpcClient client) throws IOException {
        return client.send(this, responseType);
    }
}
