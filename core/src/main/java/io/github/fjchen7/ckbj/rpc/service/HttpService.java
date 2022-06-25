package io.github.fjchen7.ckbj.rpc.service;

import com.google.gson.Gson;
import io.github.fjchen7.ckbj.rpc.GsonFactory;
import io.github.fjchen7.ckbj.rpc.Request;
import io.github.fjchen7.ckbj.rpc.Response;

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
