package org.ckbj.protocol.rpc.type;

import org.ckbj.RpcClient;

import java.io.IOException;
import java.util.List;

public class Request<S, T extends Response> {
    private String jsonrpc = "2.0";
    private String method;
    private List<S> params;
    private long id;

    private RpcClient rpcClient;
    private Class<T> responseType;

    public Request() {}


    public T send() throws IOException {
        return rpcClient.send(this, responseType);
    }
}
