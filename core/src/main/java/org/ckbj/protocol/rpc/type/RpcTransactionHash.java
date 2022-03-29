package org.ckbj.protocol.rpc.type;

public class RpcTransactionHash extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
