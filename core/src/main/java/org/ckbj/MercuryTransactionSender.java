package org.ckbj;

import org.ckbj.protocol.type.Transaction;

import java.io.IOException;

public class MercuryTransactionSender implements TransactionSender {
    private Mercury mercury;

    public MercuryTransactionSender(String url) {
    }

    @Override
    public String sendTransaction(Transaction transaction) throws IOException{
        return mercury.sendTransaction(transaction).send().getResult();
    }
}
