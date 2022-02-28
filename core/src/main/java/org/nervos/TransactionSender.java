package org.nervos;

import org.nervos.protocol.type.Transaction;

import java.io.IOException;

public interface TransactionSender {
    String sendTransaction(Transaction transaction) throws IOException;
}
