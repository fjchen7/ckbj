package org.ckbj.rpc;

import java.io.IOException;

public interface JsonRpcService {
    <T extends Response> T send(Request request, Class<T> responseType) throws IOException;
}
