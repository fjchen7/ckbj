package org.ckbj.rpc.exception;

public class JsonRpcServerErrorException extends JsonRpcException {
    public JsonRpcServerErrorException(int code, String message) {
        super(code, message);
    }
}
