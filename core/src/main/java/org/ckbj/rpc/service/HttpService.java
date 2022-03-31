package org.ckbj.rpc.service;

import com.google.gson.Gson;
import org.ckbj.rpc.GsonFactory;
import org.ckbj.rpc.Request;
import org.ckbj.rpc.Response;

import java.io.IOException;

public abstract class HttpService implements JsonRpcService {
    protected Gson gson = GsonFactory.create();

    abstract String executeRequest(String body) throws IOException;

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        String body = gson.toJson(request);
        String resp = executeRequest(body);
        T response = gson.fromJson(resp, responseType);
        return response;
    }
}
