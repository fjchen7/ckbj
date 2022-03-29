package org.ckbj;

public class TransactionBuilder {
    protected DataFetcher dataFetcher;

    public TransactionBuilder(DataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }
}
