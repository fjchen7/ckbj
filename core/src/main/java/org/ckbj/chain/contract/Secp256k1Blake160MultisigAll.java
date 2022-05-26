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
        private byte s = 0x0;
        private int r;
        private int m;
        private List<byte[]> publicKeysHash;

        private Args() {
        }

        /**
         * The format version
         */
        public byte getVersion() {
            return s;
        }

        /**
         * The first R items in public key list whose signatures must be provided for.
         */
        public int getFirstN() {
            return r;
        }

        /**
         * signature threshold
         */
        public int getThreshold() {
            return m;
        }

        /**
         * Size of public key list
         */
        public int getKeySize() {
            return publicKeysHash.size();
        }

        public List<byte[]> getKeysHash() {
            return publicKeysHash;
        }

        public byte[] multisigScript() {
            return encode();
        }

        public byte[] encode() {
            byte[] out = new byte[4 + this.publicKeysHash.size() * 20];
            out[0] = this.s;
            out[1] = (byte) (this.r & 0xff);
            out[2] = (byte) (this.m & 0xff);
            out[3] = (byte) (this.publicKeysHash.size() & 0xff);
            int pos = 4;
            for (byte[] publicKeyHash: this.publicKeysHash) {
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
            args.s = in[0];
            args.r = in[1];
            args.m = in[2];
            args.publicKeysHash = new ArrayList<>();
            for (int i = 0; i < in[3]; i++) {
                byte[] publicKeyHash = new byte[20];
                System.arraycopy(in, 4 + i * 20, publicKeyHash, 0, 20);
                args.publicKeysHash.add(publicKeyHash);
            }
            return args;
        }

        @Override
        public byte[] getWitnessPlaceholder(byte[] originalWitness) {
            byte[] multisigScript = this.encode();
            byte[] witnessLockPlaceholder = new byte[multisigScript.length + this.m * Sign.SIGNATURE_LENGTH];
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

            if (s != args.s) return false;
            if (r != args.r) return false;
            if (m != args.m) return false;

            if (publicKeysHash.size() != args.publicKeysHash.size()) return false;
            for (int i = 0; i < publicKeysHash.size(); i++) {
                if (!Arrays.equals(publicKeysHash.get(i), args.publicKeysHash.get(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = s;
            result = 31 * result + r;
            result = 31 * result + m;
            result = 31 * result + publicKeysHash.hashCode();
            return result;
        }

        public static final class Builder {
            private byte s = 0x0;
            private int r = 0;
            private int m = -1;
            private List<byte[]> publicKeyHashes = new ArrayList<>();

            private Builder() {
            }

            public Builder setVersion(byte s) {
                this.s = s;
                return this;
            }

            /**
             * The first R items in public key list whose signatures must be provided for.
             */
            public Builder setFirstN(int r) {
                if (r < 0) {
                    throw new IllegalArgumentException("firstN must be greater than or equal to 0");
                }
                this.r = r;
                return this;
            }

            /**
             * signature threshold
             */
            public Builder setThreshold(int m) {
                if (m <= 0) {
                    throw new IllegalArgumentException("Threshold must be greater than 0");
                }
                this.m = m;
                return this;
            }

            public Builder addKey(byte[] publicKeyHash) {
                if (publicKeyHash.length != 20) {
                    throw new IllegalArgumentException("Public key hash must be 20 bytes");
                }
                this.publicKeyHashes.add(publicKeyHash);
                return this;
            }

            public Builder addKey(ECKeyPair.Point publicKeys) {
                byte[] publicKeyHash = Secp256k1Blake160SighashAll.newArgs(publicKeys).getArgs();
                return addKey(publicKeyHash);
            }

            public Builder addKey(String address) {
                return addKey(Address.decode(address));
            }

            public Builder addKey(Address address) {
                Script script = address.getScript();
                if (address.getNetwork().getContractType(script) != Contract.Type.SECP256K1_BLAKE160_SIGHASH_ALL) {
                    throw new IllegalArgumentException("Address must be a secp256k1-blake160-sighash-all address");
                }
                return addKey(script.getArgs());
            }

            public Args build() {
                if (m == -1) {
                    throw new IllegalArgumentException("Not set threshold");
                }
                if (publicKeyHashes.size() == 0) {
                    throw new IllegalArgumentException("Public key hashes must not be empty");
                }
                if (publicKeyHashes.size() < m) {
                    throw new IllegalArgumentException("Size of public key hashes must be greater than or equal to threshold");
                }
                if (r > m) {
                    throw new IllegalArgumentException("The firstN must be less than or equal to threshold");
                }
                Args args = new Args();
                args.s = this.s;
                args.r = this.r;
                args.m = this.m;
                args.publicKeysHash = this.publicKeyHashes;
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
