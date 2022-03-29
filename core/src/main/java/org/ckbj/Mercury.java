package org.ckbj;

import org.ckbj.protocol.rpc.type.*;
import org.ckbj.protocol.type.Transaction;

public interface Mercury {
    Request<?, RpcTransaction> getTransactionFromAddress(String address);
    Request<?, RpcDetailedCells> getLiveCellsByAddress(String address);
    Request<?, RpcTransactionHash> sendTransaction(Transaction transaction);
}
