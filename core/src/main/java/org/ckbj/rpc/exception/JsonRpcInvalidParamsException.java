package org.ckbj.rpc.exception;

public class JsonRpcInvalidParamsException extends JsonRpcException {
    public JsonRpcInvalidParamsException(int code, String message) {
        super(code, message);
    }
}
