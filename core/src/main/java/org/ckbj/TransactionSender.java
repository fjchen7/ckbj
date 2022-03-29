package org.ckbj;

import org.ckbj.protocol.type.Transaction;

import java.io.IOException;

public interface TransactionSender {
    String sendTransaction(Transaction transaction) throws IOException;
}
