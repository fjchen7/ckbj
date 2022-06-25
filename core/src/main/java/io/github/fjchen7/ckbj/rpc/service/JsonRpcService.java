package io.github.fjchen7.ckbj.rpc.service;

import io.github.fjchen7.ckbj.rpc.Request;
import io.github.fjchen7.ckbj.rpc.Response;

import java.io.IOException;

public interface JsonRpcService {
    <T extends Response> T send(Request request, Class<T> responseType) throws IOException;
}
