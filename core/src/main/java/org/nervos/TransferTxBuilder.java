package org.nervos;

import org.nervos.protocol.type.DetailedCell;
import org.nervos.protocol.type.DetailedTransaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class TransferTxBuilder extends TransactionBuilder {
    private Config config;

    private static TransferTxBuilder TESTNET_INSTANCE;

    public TransferTxBuilder(DataFetcher dataFetcher, Config config) {
        super(dataFetcher);
        this.config = config;
    }

    public static TransferTxBuilder DefaultTestnetTransferTxBuilder() {
        if (TESTNET_INSTANCE == null) {
            DataFetcher dataFetcher = new MercuryDataFetcher("https://mercury-testnet.ckbapp.dev/");
            TESTNET_INSTANCE = new TransferTxBuilder(dataFetcher, Config.TESTNET_CONFIG);

        }
        return TESTNET_INSTANCE;
    }

    public DetailedTransaction buildTransferTx(String fromAddress, String toAddress, float amountInCKB) throws IOException {
        List<DetailedCell> liveCells = dataFetcher.getLiveCellByAddress(fromAddress);
        DetailedTransaction.DetailedTransactionBuilder txBuilder = new DetailedTransaction.DetailedTransactionBuilder(config);

        DetailedTransaction tx = txBuilder
                .addInputs(liveCells)
                .addOutput(toAddress, new BigDecimal(amountInCKB).multiply(new BigDecimal(100000000)).toBigInteger())
                .build();
        return tx;
    }
}
