package io.github.fjchen7.ckbj;

import io.github.fjchen7.ckbj.chain.Contract;
import io.github.fjchen7.ckbj.chain.Network;
import io.github.fjchen7.ckbj.chain.address.Address;
import io.github.fjchen7.ckbj.chain.contract.Secp256k1Blake160MultisigAll;
import io.github.fjchen7.ckbj.chain.contract.Secp256k1Blake160SighashAll;
import io.github.fjchen7.ckbj.chain.contract.Sudt;
import io.github.fjchen7.ckbj.crypto.ECKeyPair;
import io.github.fjchen7.ckbj.type.Script;
import io.github.fjchen7.ckbj.type.Transaction;
import io.github.fjchen7.ckbj.utils.Hex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Example {

    @Disabled
    @Test
    // on-chain tx: https://pudge.explorer.nervos.org/transaction/0xd66ea40920f875ed4e684032b8158bb90af39c6c35d2880691eab21f4e46a103
    public void singleSign() throws IOException {
        Network network = Network.TESTNET;
        ECKeyPair keyPair = ECKeyPair.create("0x4e3796fb07ef32553485f995ef6d63a66792f86ebfa431815282f3f81029adfb");
        // ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq02cgdvd5mng9924xarf3rflqzafzmzlpsuhh83c
        Address address = Secp256k1Blake160SighashAll
                .newArgs(keyPair.getPublicKey())
                .toAddress(network);

        Transaction tx = Transaction.smartBuilder(network)
                .addCellDeps(Contract.Name.SECP256K1_BLAKE160_SIGHASH_ALL)
                .addInput("0xcd10217c1de6ed69d065db9f1af2d4f364fb7e8e402c6e1909e20abdf44d6975", 1)
                .addOutputInBytes(address, 9899.0)
                .build();

        // sign tx and put signature into witnesses
        Secp256k1Blake160SighashAll.newFulfillment(keyPair).fulfill(tx, 0);

        // send transaction
        CkbService service = CkbService.getInstance(network);
        byte[] hash = service.sendTransaction(tx);
        System.out.println(Hex.toHexString(hash));
    }

    @Disabled
    @Test
    // on-chain tx: https://pudge.explorer.nervos.org/transaction/0x1935b1d79193b765b427cf4f7de53beddce7d585d85c7484744aadd1e0eb36ac
    public void multiSig() throws IOException {
        Network network = Network.TESTNET;

        // construct information of multisig script
        Secp256k1Blake160MultisigAll.Args args = Secp256k1Blake160MultisigAll.newArgsBuilder()
                .setThreshold(2)
                .addKey("ckt1qyqw4ss6cmfhxs2242d6xnzxn7q96j9k97rqn7cn8m")
                .addKey("ckt1qyqz0c9vrrft28e47nlvf0wgtt0n5d4u8fhsghaylr")
                .build();
        Address sender = args.toAddress(network);

        Transaction tx = Transaction.smartBuilder(network)
                .addCellDeps(Contract.Name.SECP256K1_BLAKE160_MULTISIG_ALL)
                .addInput("0x0f32433ed5ad6f49885f76cbb3ebaa35d920003e15cffbed74e793374503cab7", 0)
                .addOutputInBytes(sender, 99.9)
                .addOutputInBytes(sender, 9900.0)
                .build();

        // sign tx separately by key pairs
        // ckt1qyqw4ss6cmfhxs2242d6xnzxn7q96j9k97rqn7cn8m
        ECKeyPair k1 = ECKeyPair.create("0x4e3796fb07ef32553485f995ef6d63a66792f86ebfa431815282f3f81029adfb");
        byte[] signature1 = Secp256k1Blake160MultisigAll.newSigner(args, k1).sign(tx, 0);
        // ckt1qyqz0c9vrrft28e47nlvf0wgtt0n5d4u8fhsghaylr
        ECKeyPair k2 = ECKeyPair.create("0x75f53e5b6290c5f349d8a91779f3c028658d49674cb1e24cb326d71829a65ec6");
        byte[] signature2 = Secp256k1Blake160MultisigAll.newSigner(args, k2).sign(tx, 0);

        // aggregate signatures and put it into witnesses in transaction
        Secp256k1Blake160MultisigAll.newFulfillment(args, signature1, signature2)
                .fulfill(tx, 0);

        // send transaction
        CkbService service = CkbService.getInstance(Network.TESTNET);
        byte[] txHash = service.sendTransaction(tx);
        System.out.println(Hex.toHexString(txHash));
    }

    @Disabled
    @Test
    public void sendSudt() throws IOException {
        Network network = Network.TESTNET;

        Script sudtType = network.getContract(Contract.Name.SUDT)
                .createScript("0x7c7f0ee1d582c385342367792946cff3767fe02f26fd7f07dba23ae3c65b28bc");
        Transaction tx = Transaction.smartBuilder(network)
                .addCellDeps(Contract.Name.SECP256K1_BLAKE160_SIGHASH_ALL)
                .addCellDeps(Contract.Name.SUDT)
                // output with sudt type
                .addInput("0xc809e1010701cf64521170787aea1d19bd7a8793886cec5196add83c0c5bcc54", 2)
                .beginAddOutput()
                .setType(sudtType)
                .setLock("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq02cgdvd5mng9924xarf3rflqzafzmzlpsuhh83c")
                .setCapacityInBytes(142)
                .setData(Sudt.amountToData(969999992760L))
                .endAddOutput()
                .build();

        // sign tx and put signature into witnesses
        // ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqd0pdquvfuq077aemn447shf4d8u5f4a0glzz2g4
        ECKeyPair keyPair = ECKeyPair.create("9d8ca87d75d150692211fa62b0d30de4d1ee6c530d5678b40b8cedacf0750d0f");
        Secp256k1Blake160SighashAll.newFulfillment(keyPair).fulfill(tx, 0);

        // send transaction
        CkbService service = CkbService.getInstance(network);
        byte[] hash = service.sendTransaction(tx);
        System.out.println(Hex.toHexString(hash));
    }

    @Disabled
    @Test
    // on-chain tx: https://pudge.explorer.nervos.org/transaction/0x293491e3c7763a8b03bf8eb7bad4a61c3951c09091f16765a38a8126a45764c2
    public void issueSudt() throws IOException {
        Network network = Network.TESTNET;
        String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq02cgdvd5mng9924xarf3rflqzafzmzlpsuhh83c";
        // args of sudt should be hash of input lock script
        Script sudtType = new Sudt.Args(sender).toScript(Network.TESTNET);
        Transaction tx = Transaction.smartBuilder(network)
                .addCellDeps(Contract.Name.SECP256K1_BLAKE160_SIGHASH_ALL)
                .addCellDeps(Contract.Name.SUDT)
                // plain output without any type
                .addInput("0x4c0740007221f8c6baa7a6b9eb04f6db9af08b1fcdd720d6449e5f8f57ec6a7d", 0)
                .beginAddOutput()
                .setType(sudtType)
                .setLock("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq02cgdvd5mng9924xarf3rflqzafzmzlpsuhh83c")
                .setCapacityInBytes(199)
                .setData(Sudt.amountToData(1000000000000L))
                .endAddOutput()
                .addOutputInBytes(sender, 9800)
                .build();

        // sign tx and put signature into witnesses
        // ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqd0pdquvfuq077aemn447shf4d8u5f4a0glzz2g4
        ECKeyPair keyPair = ECKeyPair.create("0x4e3796fb07ef32553485f995ef6d63a66792f86ebfa431815282f3f81029adfb");
        Secp256k1Blake160SighashAll.newFulfillment(keyPair).fulfill(tx, 0);

        // send transaction
        CkbService service = CkbService.getInstance(network);
        byte[] hash = service.sendTransaction(tx);
        System.out.println(Hex.toHexString(hash));
    }
}
