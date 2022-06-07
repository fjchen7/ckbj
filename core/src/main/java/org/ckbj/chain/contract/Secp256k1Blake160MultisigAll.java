package org.ckbj.chain.contract;

import org.ckbj.chain.Contract;
import org.ckbj.chain.StandardLockContractArgs;
import org.ckbj.chain.address.Address;
import org.ckbj.crypto.Blake2b;
import org.ckbj.crypto.ECKeyPair;
import org.ckbj.crypto.Sign;
import org.ckbj.type.Script;
import org.ckbj.type.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Secp256k1Blake160MultisigAll {
    public static Args.Builder newArgsBuilder() {
        return new Args.Builder();
    }

    public static Fulfillment newFulfillment(Args args, byte[]... signatures) {
        return new Fulfillment(args, signatures);
    }

    public static Signer newSigner(Args args, ECKeyPair keyPair) {
        return new Signer(args, keyPair);
    }

    /**
     * https://github.com/nervosnetwork/ckb-system-scripts/wiki/How-to-sign-transaction#multisig
     */
    public static class Args implements StandardLockContractArgs {
        private int version = 0x0;
        private int firstN;
        private int threshold;
        private List<byte[]> keysHashes;

        private Args() {
        }

        /**
         * The format version
         */
        public int getVersion() {
            return version;
        }

        /**
         * The first R items in public key list which signatures must be provided for.
         */
        public int getFirstN() {
            return firstN;
        }

        /**
         * signature threshold
         */
        public int getThreshold() {
            return threshold;
        }

        /**
         * Size of public key list
         */
        public int getKeySize() {
            return keysHashes.size();
        }

        public List<byte[]> getKeysHashes() {
            return keysHashes;
        }

        public byte[] multisigScript() {
            return encode();
        }

        public byte[] encode() {
            byte[] out = new byte[4 + this.keysHashes.size() * 20];
            out[0] = (byte) (this.version & 0xff);
            out[1] = (byte) (this.firstN & 0xff);
            out[2] = (byte) (this.threshold & 0xff);
            out[3] = (byte) (this.keysHashes.size() & 0xff);
            int pos = 4;
            for (byte[] publicKeyHash: this.keysHashes) {
                System.arraycopy(publicKeyHash, 0, out, pos, 20);
                pos += 20;
            }
            return out;
        }

        public static Args decode(byte[] in) {
            if ((in.length - 4) % 20 != 0) {
                throw new IllegalArgumentException("Invalid bytes length");
            }
            if ((in.length - 4) / 20 != in[3]) {
                throw new IllegalArgumentException("Invalid public key list size");
            }
            Args args = new Args();
            args.version = in[0];
            args.firstN = in[1];
            args.threshold = in[2];
            args.keysHashes = new ArrayList<>();
            for (int i = 0; i < in[3]; i++) {
                byte[] publicKeyHash = new byte[20];
                System.arraycopy(in, 4 + i * 20, publicKeyHash, 0, 20);
                args.keysHashes.add(publicKeyHash);
            }
            return args;
        }

        @Override
        public byte[] getWitnessPlaceholder(byte[] originalWitness) {
            byte[] multisigScript = this.encode();
            byte[] witnessLockPlaceholder = new byte[multisigScript.length + this.threshold * Sign.SIGNATURE_LENGTH];
            System.arraycopy(multisigScript, 0, witnessLockPlaceholder, 0, multisigScript.length);
            byte[] witnessPlaceholder = StandardLockContractArgs
                    .setWitnessArgsLock(originalWitness, witnessLockPlaceholder);
            return witnessPlaceholder;
        }

        @Override
        public byte[] getArgs() {
            byte[] hash = Blake2b.digest(encode());
            return Arrays.copyOfRange(hash, 0, 20);
        }

        @Override
        public Contract.Type getContractType() {
            return Contract.Type.SECP256K1_BLAKE160_MULTISIG_ALL;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Args args = (Args) o;

            if (version != args.version) return false;
            if (firstN != args.firstN) return false;
            if (threshold != args.threshold) return false;

            if (keysHashes.size() != args.keysHashes.size()) return false;
            for (int i = 0; i < keysHashes.size(); i++) {
                if (!Arrays.equals(keysHashes.get(i), args.keysHashes.get(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = version;
            result = 31 * result + firstN;
            result = 31 * result + threshold;
            result = 31 * result + keysHashes.hashCode();
            return result;
        }

        public static final class Builder {
            private byte version = 0x0;
            private int firstN = 0;
            private int threshold = -1;
            private List<byte[]> keyHashes = new ArrayList<>();

            private Builder() {
            }

            public Builder setVersion(byte version) {
                if (version < 0 || version > 255) {
                    throw new IllegalArgumentException("Out of version range [0, 255]");
                }
                this.version = version;
                return this;
            }

            /**
             * The first N items in public key list which signatures must be provided for.
             */
            public Builder setFirstN(int firstN) {
                if (firstN < 0 || firstN > 255) {
                    throw new IllegalArgumentException("Out of firstN range [0, 255]");
                }
                this.firstN = firstN;
                return this;
            }

            /**
             * signature threshold
             */
            public Builder setThreshold(int threshold) {
                if (threshold <= 0) {
                    throw new IllegalArgumentException("Threshold must be greater than 0");
                }
                this.threshold = threshold;
                return this;
            }

            public Builder addKey(byte[] publicKeyHash) {
                if (publicKeyHash.length != 20) {
                    throw new IllegalArgumentException("Public key hash must be 20 bytes");
                }
                this.keyHashes.add(publicKeyHash);
                return this;
            }

            public Builder addKey(ECKeyPair keyPair) {
                return addKey(keyPair.getPublicKey());
            }

            public Builder addKey(ECKeyPair.Point publicKey) {
                byte[] publicKeyHash = new Secp256k1Blake160SighashAll.Args(publicKey).getArgs();
                return addKey(publicKeyHash);
            }

            /**
             * Add public key hash by a address
             *
             * @param address a secp256-blake160-sighash-all or ACP (anyone can pay) address
             * @return The builder itself
             */
            public Builder addKey(String address) {
                return addKey(Address.decode(address));
            }

            /**
             * Add public key hash by a address
             *
             * @param address a secp256-blake160-sighash-all or ACP (anyone can pay) address
             * @return The builder itself
             */
            public Builder addKey(Address address) {
                Script script = address.getScript();
                Contract.Type contractType = address.getNetwork().getContractType(script);
                byte[] publicKeyHash = new byte[20];
                switch (contractType) {
                    case SECP256K1_BLAKE160_SIGHASH_ALL:
                    case ANYONE_CAN_PAY:
                        System.arraycopy(script.getArgs(), 0, publicKeyHash, 0, 20);
                        break;
                    default:
                        throw new IllegalArgumentException("Only accept secp256k1-blake160-sighash-all or ACP address");
                }
                return addKey(publicKeyHash);
            }

            public Args build() {
                if (threshold == -1) {
                    throw new IllegalArgumentException("Not set threshold");
                }
                if (keyHashes.size() == 0) {
                    throw new IllegalArgumentException("Public key hashes must not be empty");
                }
                if (keyHashes.size() > 255) {
                    throw new IllegalArgumentException("Public key hashes size must not be greater than 255");
                }
                if (keyHashes.size() < threshold) {
                    throw new IllegalArgumentException("Size of public key hashes must be greater than or equal to threshold");
                }
                if (firstN > threshold) {
                    throw new IllegalArgumentException("The firstN must be less than or equal to threshold");
                }
                Args args = new Args();
                args.version = this.version;
                args.firstN = this.firstN;
                args.threshold = this.threshold;
                args.keysHashes = this.keyHashes;
                return args;
            }
        }
    }

    public static class Fulfillment extends AbstractStandardLockScriptFulfillment {
        private final Args args;
        private final byte[][] signatures;

        protected Fulfillment(Args args, byte[]... signatures) {
            this.args = args;
            if (signatures.length != args.getThreshold()) {
                throw new IllegalArgumentException("Number of signatures must be equal to multisig threshold");
            }
            this.signatures = signatures;
        }

        @Override
        public Contract.Type getContractType() {
            return Contract.Type.SECP256K1_BLAKE160_MULTISIG_ALL;
        }

        @Override
        protected boolean doMatch(byte[] scriptArgs) {
            byte[] args = this.args.getArgs();
            return Arrays.equals(args, scriptArgs);
        }

        @Override
        public void fulfill(Transaction transaction, int... inputGroup) {
            byte[] lock = aggregateSignatures(args, signatures);

            List<byte[]> witnesses = transaction.getWitnesses();
            int firstIndex = inputGroup[0];
            byte[] witness = StandardLockContractArgs.setWitnessArgsLock(witnesses.get(firstIndex), lock);
            witnesses.set(firstIndex, witness);
        }

        public byte[] aggregateSignatures(Args args, byte[]... signatures) {
            if (signatures == null || signatures.length != args.getThreshold()) {
                throw new IllegalArgumentException("Number of signatures must be equal to multisig threshold");
            }
            byte[] multiScript = args.multisigScript();
            byte[] aggregatedSignature = new byte[multiScript.length + Sign.SIGNATURE_LENGTH * signatures.length];
            System.arraycopy(multiScript, 0, aggregatedSignature, 0, multiScript.length);
            int pos = 4 + 20 * signatures.length;
            for (int i = 0; i < signatures.length; i++) {
                byte[] signature = signatures[i];
                if (signature.length != Sign.SIGNATURE_LENGTH) {
                    throw new IllegalArgumentException("Signature length should be " + Sign.SIGNATURE_LENGTH);
                }
                System.arraycopy(signature, 0, aggregatedSignature, pos, Sign.SIGNATURE_LENGTH);
                pos += Sign.SIGNATURE_LENGTH;
            }
            return aggregatedSignature;
        }
    }

    public static class Signer {
        private final Args args;
        private final ECKeyPair keyPair;

        protected Signer(Args args, ECKeyPair keyPair) {
            this.args = args;
            this.keyPair = keyPair;
        }

        public byte[] sign(Transaction transaction, int... inputGroup) {
            byte[] originalWitness = transaction.getWitness((inputGroup[0]));
            byte[] witnessPlaceholder = args.getWitnessPlaceholder(originalWitness);
            Secp256k1Blake160SighashAll.Signer signer = new Secp256k1Blake160SighashAll.Signer(keyPair);
            return signer.sign(transaction, witnessPlaceholder, inputGroup);
        }
    }
}
