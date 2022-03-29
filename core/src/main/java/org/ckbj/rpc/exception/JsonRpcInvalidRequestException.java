package org.ckbj.rpc.exception;

public class JsonRpcInvalidRequestException extends JsonRpcException {
    public JsonRpcInvalidRequestException(int code, String message) {
        super(code, message);
    }
}
