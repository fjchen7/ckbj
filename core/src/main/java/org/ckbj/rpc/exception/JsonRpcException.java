package org.ckbj.rpc.exception;

import org.ckbj.rpc.Response;

public class JsonRpcException extends RuntimeException {
    protected final int code;

    public JsonRpcException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static JsonRpcException newException(Response.Error error) {
        return newException(error.getCode(), error.getMessage());
    }

    public static JsonRpcException newException(int code, String message) {
        if (code == -32700) {
            return new JsonRpcParseException(code, message);
        } else if (code == -32600) {
            return new JsonRpcInvalidRequestException(code, message);
        } else if (code == -32601) {
            return new JsonRpcMethodNotFoundException(code, message);
        } else if (code == -32602) {
            return new JsonRpcInvalidParamsException(code, message);
        } else if (code == -32603) {
            return new JsonRpcInternalErrorException(code, message);
        } else if (code >= -32000 && code <= -32099) {
            return new JsonRpcServerErrorException(code, message);
        } else {
            return new JsonRpcException(code, message);
        }
    }
}
