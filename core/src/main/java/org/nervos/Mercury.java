package org.nervos;

import org.nervos.protocol.rpc.type.*;
import org.nervos.protocol.type.Transaction;

public interface Mercury {
    Request<?, RpcTransaction> getTransactionFromAddress(String address);
    Request<?, RpcDetailedCells> getLiveCellsByAddress(String address);
    Request<?, RpcTransactionHash> sendTransaction(Transaction transaction);
}
