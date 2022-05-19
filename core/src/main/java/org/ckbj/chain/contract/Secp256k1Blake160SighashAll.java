package org.ckbj.chain.contract;

import org.ckbj.chain.Contract;
import org.ckbj.chain.Network;
import org.ckbj.chain.address.Address;
import org.ckbj.crypto.Blake2b;
import org.ckbj.crypto.ECKeyPair;
import org.ckbj.crypto.Sign;
import org.ckbj.molecule.Serializer;
import org.ckbj.type.Script;
import org.ckbj.type.Transaction;

import java.util.Arrays;
import java.util.List;

import static org.ckbj.chain.contract.StandardLockScriptFulfillment.setWitnessArgsLock;

public class Secp256k1Blake160SighashAll {
    public static Address createAddress(Network network, ECKeyPair.Point publicKey) {
        byte[] args = createArgs(publicKey);
        Script script = network.getContract(Contract.Type.SECP256K1_BLAKE160_SIGHASH_ALL).createScript(args);
        return new Address(script, network);
    }

    public static byte[] createArgs(ECKeyPair.Point publicKey) {
        // CKB uses encoded public keys of compressed form (with prefix 0x04)
        // See https://en.bitcoin.it/wiki/Elliptic_Curve_Digital_Signature_Algorithm for details
        byte[] publicKeyBytes = publicKey.encode(true);
        byte[] hash = Blake2b.digest(publicKeyBytes);
        return Arrays.copyOfRange(hash, 0, 20);
    }

    public static Fulfillment fulfillment(ECKeyPair ecKeyPair) {
        return new Fulfillment(ecKeyPair);
    }

    public static Signer signer(ECKeyPair ecKeyPair) {
        return new Signer(ecKeyPair);
    }

    public static class Fulfillment extends StandardLockScriptFulfillment {
        private final ECKeyPair keyPair;

        protected Fulfillment(ECKeyPair keyPair) {
            super();
            this.keyPair = keyPair;
        }

        @Override
        public Contract.Type getContractType() {
            return Contract.Type.SECP256K1_BLAKE160_SIGHASH_ALL;
        }

        @Override
        protected boolean doMatch(byte[] scriptArgs) {
            byte[] args = createArgs(keyPair.getPublicKey());
            return Arrays.equals(args, scriptArgs);
        }

        @Override
        public void fulfill(Transaction transaction, int... inputGroup) {
            byte[] lock = new Signer(keyPair).sign(transaction, inputGroup);
            List<byte[]> witnesses = transaction.getWitnesses();
            int firstIndex = inputGroup[0];
            byte[] witness = setWitnessArgsLock(witnesses.get(firstIndex), lock);
            witnesses.set(firstIndex, witness);
        }
    }

    public static class Signer {
        private ECKeyPair keyPair;

        protected Signer(ECKeyPair keyPair) {
            this.keyPair = keyPair;
        }

        public byte[] sign(Transaction transaction, byte[] witnessPlaceholder, int... inputGroup) {
            if (inputGroup.length == 0) {
                throw new IllegalArgumentException("inputGroup must not be empty");
            }

            byte[] txHash = transaction.hash();
            Blake2b blake2b = new Blake2b();
            blake2b.update(txHash);

            blake2b.update(Serializer.serialize(witnessPlaceholder.length, Serializer.MoleculeNumber.UINT64));
            blake2b.update(witnessPlaceholder);

            List<byte[]> witnesses = transaction.getWitnesses();
            for (int i = 1; i < inputGroup.length; i++) {
                int index = inputGroup[i];
                byte[] witness = witnesses.get(index);
                blake2b.update(Serializer.serialize(witness.length, Serializer.MoleculeNumber.UINT64));
                blake2b.update(witness);
            }
            for (int i = transaction.getInputs().size(); i < transaction.getWitnesses().size(); i++) {
                byte[] witness = witnesses.get(i);
                blake2b.update(Serializer.serialize(witness.length, Serializer.MoleculeNumber.UINT64));
                blake2b.update(witness);
            }

            byte[] digest = blake2b.doFinal();
            byte[] signature = Sign.signMessage(digest, keyPair, false).getSignature();
            return signature;
        }

        public byte[] sign(Transaction transaction, int... inputGroup) {
            byte[] witness = transaction.getWitness(inputGroup[0]);
            witness = setWitnessArgsLock(witness, new byte[Sign.SIGNATURE_LENGTH]);
            return sign(transaction, witness, inputGroup);
        }
    }
}
