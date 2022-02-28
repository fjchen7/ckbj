package org.nervos;

import org.nervos.protocol.rpc.type.Request;
import org.nervos.protocol.rpc.type.Response;

import java.io.IOException;

public interface RpcClient {
    <T extends Response> T send(Request request, Class<T> responseType) throws IOException;
}
