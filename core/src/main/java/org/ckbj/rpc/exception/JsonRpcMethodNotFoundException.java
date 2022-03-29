package org.ckbj.rpc.exception;

public class JsonRpcMethodNotFoundException extends JsonRpcException {
    public JsonRpcMethodNotFoundException(int code, String message) {
        super(code, message);
    }
}
