package org.ckbj.chain.contract;

import org.ckbj.chain.Contract;
import org.ckbj.chain.StandardLockContractArgs;
import org.ckbj.crypto.Blake2b;
import org.ckbj.crypto.ECKeyPair;
import org.ckbj.crypto.Sign;
import org.ckbj.molecule.Serializer;
import org.ckbj.type.Transaction;
import org.ckbj.type.WitnessArgs;

import java.util.Arrays;
import java.util.List;

public class Secp256k1Blake160SighashAll {
    public static Args newArgs(ECKeyPair.Point publicKey) {
        return new Args(publicKey);
    }

    public static Args newArgs(byte[] publicKeyHash) {
        return new Args(publicKeyHash);
    }

    public static Fulfillment newFulfillment(ECKeyPair ecKeyPair) {
        return new Fulfillment(ecKeyPair);
    }

    public static Signer newSigner(ECKeyPair ecKeyPair) {
        return new Signer(ecKeyPair);
    }

    public static class Args implements StandardLockContractArgs {
        private byte[] publicKeyHash;

        public Args(ECKeyPair.Point publicKey) {
            // CKB uses encoded public keys of compressed form (with prefix 0x04)
            // See https://en.bitcoin.it/wiki/Elliptic_Curve_Digital_Signature_Algorithm for details
            publicKeyHash = Blake2b.digest(publicKey.encode(true));
            publicKeyHash = Arrays.copyOfRange(publicKeyHash, 0, 20);
        }

        public Args(byte[] publicKeyHash) {
            if (publicKeyHash.length != 20) {
                throw new IllegalArgumentException("publicKeyHash must be 20 bytes");
            }
            this.publicKeyHash = publicKeyHash;
        }

        @Override
        public byte[] getArgs() {
            return publicKeyHash;
        }

        @Override
        public Contract.Name getContractName() {
            return Contract.Name.SECP256K1_BLAKE160_SIGHASH_ALL;
        }

        @Override
        public byte[] getWitnessPlaceholder(byte[] originalWitness) {
            WitnessArgs witnessArgs = WitnessArgs.decode(originalWitness);
            witnessArgs.setLock(new byte[Sign.SIGNATURE_LENGTH]);
            return witnessArgs.encode();
        }
    }

    public static class Fulfillment extends AbstractStandardLockScriptFulfillment {
        private final ECKeyPair keyPair;

        protected Fulfillment(ECKeyPair keyPair) {
            super();
            this.keyPair = keyPair;
        }

        @Override
        public Contract.Name getContractName() {
            return Contract.Name.SECP256K1_BLAKE160_SIGHASH_ALL;
        }

        @Override
        protected boolean doMatch(byte[] scriptArgs) {
            byte[] args = new Args(keyPair.getPublicKey()).getArgs();
            return Arrays.equals(args, scriptArgs);
        }

        @Override
        public void fulfill(Transaction transaction, int... inputGroup) {
            byte[] lock = new Signer(keyPair).sign(transaction, inputGroup);
            int index = inputGroup[0];
            WitnessArgs witnessArgs = WitnessArgs.decode(transaction.getWitness(index));
            witnessArgs.setLock(lock);
            transaction.setWitness(index, witnessArgs.encode());
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
            witness = new Args(keyPair.getPublicKey()).getWitnessPlaceholder(witness);
            return sign(transaction, witness, inputGroup);
        }
    }
}
