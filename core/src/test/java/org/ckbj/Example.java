package org.ckbj;

import org.ckbj.chain.Contract;
import org.ckbj.chain.Network;
import org.ckbj.chain.address.Address;
import org.ckbj.chain.contract.Secp256k1Blake160MultisigAll;
import org.ckbj.chain.contract.Secp256k1Blake160SighashAll;
import org.ckbj.crypto.ECKeyPair;
import org.ckbj.type.Transaction;
import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Example {

    @Disabled
    @Test
    // onchain tx: https://pudge.explorer.nervos.org/transaction/0xd66ea40920f875ed4e684032b8158bb90af39c6c35d2880691eab21f4e46a103
    public void singleSign() throws IOException {
        Network network = Network.TESTNET;
        ECKeyPair keyPair = ECKeyPair.create("0x4e3796fb07ef32553485f995ef6d63a66792f86ebfa431815282f3f81029adfb");
        Address address = Secp256k1Blake160SighashAll
                .newArgs(keyPair.getPublicKey())
                .toAddress(network);

        Transaction tx = Transaction.builder(network)
                .addCellDep(Contract.Type.SECP256K1_BLAKE160_SIGHASH_ALL)
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
    // onchain tx: https://pudge.explorer.nervos.org/transaction/0x1935b1d79193b765b427cf4f7de53beddce7d585d85c7484744aadd1e0eb36ac
    public void multiSig() throws IOException {
        Network network = Network.TESTNET;

        // construct information of multisig script
        Secp256k1Blake160MultisigAll.Args args = Secp256k1Blake160MultisigAll.newArgsBuilder()
                .setThreshold(2)
                .addKey("ckt1qyqw4ss6cmfhxs2242d6xnzxn7q96j9k97rqn7cn8m")
                .addKey("ckt1qyqz0c9vrrft28e47nlvf0wgtt0n5d4u8fhsghaylr")
                .build();
        Address sender = args.toAddress(network);
        //        Address sender = Secp256k1Blake160MultisigAll.createAddress(network, args);

        Transaction tx = Transaction.builder(network)
                .addCellDep(Contract.Type.SECP256K1_BLAKE160_MULTISIG_ALL)
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

}
