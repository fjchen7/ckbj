package org.ckbj.rpc.exception;

public class JsonRpcParseException extends JsonRpcException {
    public JsonRpcParseException(int code, String message) {
        super(code, message);
    }
}
