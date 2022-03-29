package org.ckbj.rpc.exception;

public class JsonRpcInternalErrorException extends JsonRpcException {
    public JsonRpcInternalErrorException(int code, String message) {
        super(code, message);
    }
}
