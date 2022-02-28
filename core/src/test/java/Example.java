import org.junit.jupiter.api.Test;
import org.nervos.*;
import org.nervos.protocol.type.DetailedTransaction;
import org.nervos.sign.TransactionUnlocker;

import java.io.IOException;
import java.math.BigInteger;

public class Example {
    @Test
    void example() throws IOException {
        TransferTxBuilder txBuilder = TransferTxBuilder.DefaultTestnetTransferTxBuilder();

        // build raw transaction
        String fromAddress = "ckt1qzqascycp3h68jxgxxjvn907fq3a53hk6efwtplt6764lxleqmg4sq068tl8dh4h49jzsvfkphc8c7s5s0ykad5m9pgpg59nvn8cevhl9cl2dgveyye34j9r7lkxhjhgpmm5dzzwkt9l07xg0hmjrf4us7t4s0e9qqqqqqqqqqvcw9h3";
        String toAddress = "ckt1qyqr79tnk3pp34xp92gerxjc4p3mus2690psf0dd70";
        DetailedTransaction tx = txBuilder.buildTransferTx(fromAddress, toAddress, 129);

        // sign transaction
        TransactionUnlocker transactionUnlocker = new TransactionUnlocker()
                .registerSecp256k1Blake160Address(fromAddress, "my_private_key");
        transactionUnlocker.unlock(tx);

        // send transaction
        TransactionSender txSender = new MercuryTransactionSender("https://mercury-testnet.ckbapp.dev/");
        String txHash = txSender.sendTransaction(tx.toTransaction());
    }
}
