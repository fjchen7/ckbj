package org.ckbj.rpc;

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
            return new ParseError(code, message);
        } else if (code == -32600) {
            return new InvalidRequest(code, message);
        } else if (code == -32601) {
            return new MethodNotFound(code, message);
        } else if (code == -32602) {
            return new InvalidParams(code, message);
        } else if (code == -32603) {
            return new InternalError(code, message);
        } else if (code >= -32000 && code <= -32099) {
            return new ServerError(code, message);
        } else {
            return new JsonRpcException(code, message);
        }
    }

    public static class ParseError extends JsonRpcException {
        public ParseError(int code, String message) {
            super(code, message);
        }
    }

    public static class InvalidRequest extends JsonRpcException {
        public InvalidRequest(int code, String message) {
            super(code, message);
        }
    }

    public static class MethodNotFound extends JsonRpcException {
        public MethodNotFound(int code, String message) {
            super(code, message);
        }
    }

    public static class InvalidParams extends JsonRpcException {
        public InvalidParams(int code, String message) {
            super(code, message);
        }
    }

    public static class InternalError extends JsonRpcException {
        public InternalError(int code, String message) {
            super(code, message);
        }
    }

    public static class ServerError extends JsonRpcException {
        public ServerError(int code, String message) {
            super(code, message);
        }
    }

}
