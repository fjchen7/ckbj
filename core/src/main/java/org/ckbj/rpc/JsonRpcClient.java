package org.ckbj.rpc;

import org.ckbj.rpc.Request;
import org.ckbj.rpc.Response;

import java.io.IOException;

public interface JsonRpcClient {
    <T extends Response> T send(Request request, Class<T> responseType) throws IOException;
}
