package org.ckbj.chain.address;

import org.ckbj.chain.Network;
import org.ckbj.chain.contract.AnyoneCanPay;
import org.ckbj.chain.contract.Secp256k1Blake160MultisigAll;
import org.ckbj.chain.contract.Secp256k1Blake160SighashAll;
import org.ckbj.crypto.ECKeyPair;

public class AddressFactory {
    private Network network;

    public AddressFactory(Network network) {
        this.network = network;
    }

    /**
     * Create a secp256k1Blake160SighashAll address
     */
    public Address newAddress(ECKeyPair keyPair) {
        return newAddress(keyPair.getPublicKey());
    }

    /**
     * Create a secp256k1Blake160SighashAll address
     */
    public Address newAddress(ECKeyPair.Point publicKey) {
        Address address = new Secp256k1Blake160SighashAll.Args(publicKey)
                .toAddress(Network.TESTNET);
        return address;
    }

    /**
     * Create a secp256k1Blake160MultisigAll address
     */
    public Address newMultiSigAddress(Secp256k1Blake160MultisigAll.Args args) {
        Address address = args.toAddress(network);
        return address;
    }

    public Address newMultiSigAddress(int threshold, int firstN, byte[]... publicKeysHashes) {
        Secp256k1Blake160MultisigAll.Args.Builder builder = Secp256k1Blake160MultisigAll.newArgsBuilder();
        builder.setThreshold(threshold);
        builder.setFirstN(firstN);
        for (byte[] publicKeyHash: publicKeysHashes) {
            builder.addKey(publicKeyHash);
        }
        return newMultiSigAddress(builder.build());
    }

    public Address newMultiSigAddress(int threshold, int firstN, String... addresses) {
        Secp256k1Blake160MultisigAll.Args.Builder builder = Secp256k1Blake160MultisigAll.newArgsBuilder();
        builder.setThreshold(threshold);
        builder.setFirstN(firstN);
        for (String address: addresses) {
            builder.addKey(address);
        }
        return newMultiSigAddress(builder.build());
    }

    public Address newMultiSigAddress(int threshold, int firstN, Address... addresses) {
        Secp256k1Blake160MultisigAll.Args.Builder builder = Secp256k1Blake160MultisigAll.newArgsBuilder();
        builder.setThreshold(threshold);
        builder.setFirstN(firstN);
        for (Address address: addresses) {
            builder.addKey(address);
        }
        return newMultiSigAddress(builder.build());
    }

    /**
     * Create an ACP (Anyone-can-pay) address
     */
    public Address newAcpAddress(ECKeyPair keyPair) {
        AnyoneCanPay.Args args = AnyoneCanPay.newArgsBuilder()
                .setPublicKeyHash(keyPair.getPublicKey())
                .build();
        return args.toAddress(network);
    }

    public Address newAcpAddress(ECKeyPair.Point publicKey) {
        AnyoneCanPay.Args args = AnyoneCanPay.newArgsBuilder()
                .setPublicKeyHash(publicKey)
                .build();
        return args.toAddress(network);
    }

    public Address newAcpAddress(String secp256k1Blake160Address) {
        AnyoneCanPay.Args args = AnyoneCanPay.newArgsBuilder()
                .setPublicKeyHash(secp256k1Blake160Address)
                .build();
        return args.toAddress(network);
    }

    public Address newAcpAddress(Address secp256k1Blake160Address) {
        AnyoneCanPay.Args args = AnyoneCanPay.newArgsBuilder()
                .setPublicKeyHash(secp256k1Blake160Address)
                .build();
        return args.toAddress(network);
    }
}
