package org.ckbj.rpc;

import org.ckbj.rpc.JsonRpcClient;
import org.ckbj.rpc.Request;
import org.ckbj.rpc.Response;

import java.io.IOException;

public abstract class JsonRpcHttpClient implements JsonRpcClient {

    // TODO
    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        return null;
    }
}
