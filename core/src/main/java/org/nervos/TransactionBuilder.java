package org.nervos;

public class TransactionBuilder {
    protected DataFetcher dataFetcher;

    public TransactionBuilder(DataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }
}
