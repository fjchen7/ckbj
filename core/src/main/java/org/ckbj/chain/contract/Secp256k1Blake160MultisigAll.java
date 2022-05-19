package org.ckbj.chain.contract;

import org.ckbj.chain.Contract;
import org.ckbj.chain.Network;
import org.ckbj.chain.address.Address;
import org.ckbj.crypto.Blake2b;
import org.ckbj.crypto.ECKeyPair;
import org.ckbj.crypto.Sign;
import org.ckbj.type.Script;
import org.ckbj.type.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ckbj.chain.contract.StandardLockScriptFulfillment.setWitnessArgsLock;

public class Secp256k1Blake160MultisigAll {
    public static Address createAddress(Network network, Rule rule) {
        byte[] args = createArgs(rule);
        Script script = network.getContract(Contract.Type.SECP256K1_BLAKE160_MULTISIG_ALL).createScript(args);
        return new Address(script, network);
    }

    public static byte[] createArgs(int threshold, String... secp256k1Blake160Addresses) {
        Rule rule = Rule.builder()
                .setThreshold(threshold)
                .addKey(secp256k1Blake160Addresses)
                .build();
        return createArgs(rule);
    }

    public static byte[] createArgs(Rule rule) {
        byte[] hash = Blake2b.digest(rule.encode());
        return Arrays.copyOfRange(hash, 0, 20);
    }

    public static Fulfillment fulfillment(Rule rule, byte[]... signatures) {
        return new Fulfillment(rule, signatures);
    }

    public static Signer signer(Rule rule, ECKeyPair keyPair) {
        return new Signer(rule, keyPair);
    }

    /**
     * https://github.com/nervosnetwork/ckb-system-scripts/wiki/How-to-sign-transaction#multisig
     */
    public static class Rule {
        private byte s = 0x0;
        private int r;
        private int m;
        private List<byte[]> publicKeysHash;

        private Rule() {
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

        public static Builder builder() {
            return new Builder();
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

        public static Rule decode(byte[] in) {
            if ((in.length - 4) % 20 != 0) {
                throw new IllegalArgumentException("Invalid bytes length");
            }
            if ((in.length - 4) / 20 != in[3]) {
                throw new IllegalArgumentException("Invalid public key list size");
            }
            Rule rule = new Rule();
            rule.s = in[0];
            rule.r = in[1];
            rule.m = in[2];
            rule.publicKeysHash = new ArrayList<>();
            for (int i = 0; i < in[3]; i++) {
                byte[] publicKeyHash = new byte[20];
                System.arraycopy(in, 4 + i * 20, publicKeyHash, 0, 20);
                rule.publicKeysHash.add(publicKeyHash);
            }
            return rule;
        }

        public byte[] getWitnessLockPlaceHolder() {
            byte[] multisigScript = this.encode();
            byte[] lockPlaceholder = new byte[multisigScript.length + this.m * Sign.SIGNATURE_LENGTH];
            System.arraycopy(multisigScript, 0, lockPlaceholder, 0, multisigScript.length);
            return lockPlaceholder;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Rule rule = (Rule) o;

            if (s != rule.s) return false;
            if (r != rule.r) return false;
            if (m != rule.m) return false;

            if (publicKeysHash.size() != rule.publicKeysHash.size()) return false;
            for (int i = 0; i < publicKeysHash.size(); i++) {
                if (!Arrays.equals(publicKeysHash.get(i), rule.publicKeysHash.get(i))) {
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

            public Builder addKey(byte[]... publicKeyHashes) {
                for (int i = 0; i < publicKeyHashes.length; i++) {
                    byte[] publicKeyHash = publicKeyHashes[i];
                    if (publicKeyHash.length != 20) {
                        throw new IllegalArgumentException("Public key hash must be 20 bytes");
                    }
                    this.publicKeyHashes.add(publicKeyHash);
                }
                return this;
            }

            public Builder addKey(ECKeyPair.Point... publicKeys) {
                byte[][] publicKeyHashes = new byte[publicKeys.length][];
                for (int i = 0; i < publicKeys.length; i++) {
                    publicKeyHashes[i] = Secp256k1Blake160SighashAll.createArgs(publicKeys[i]);
                }
                return addKey(publicKeyHashes);
            }

            public Builder addKey(String... addresses) {
                Address[] arr = new Address[addresses.length];
                for (int i = 0; i < addresses.length; i++) {
                    arr[i] = Address.decode(addresses[i]);
                }
                return addKey(arr);
            }

            public Builder addKey(Address... addresses) {
                byte[][] arr = new byte[addresses.length][];
                for (int i = 0; i < addresses.length; i++) {
                    Address address = addresses[i];
                    Script script = address.getScript();
                    if (address.getNetwork().getContractType(script) != Contract.Type.SECP256K1_BLAKE160_SIGHASH_ALL) {
                        throw new IllegalArgumentException("Address must be a secp256k1-blake160-sighash-all address");
                    }
                    arr[i] = script.getArgs();
                }
                return addKey(arr);
            }

            public Rule build() {
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
                Rule rule = new Rule();
                rule.s = this.s;
                rule.r = this.r;
                rule.m = this.m;
                rule.publicKeysHash = this.publicKeyHashes;
                return rule;
            }
        }
    }

    public static class Fulfillment extends StandardLockScriptFulfillment {
        private final Rule rule;
        private final byte[][] signatures;

        public Fulfillment(Rule rule, byte[]... signatures) {
            this.rule = rule;
            if (signatures.length != rule.getThreshold()) {
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
            byte[] args = createArgs(rule);
            return Arrays.equals(args, scriptArgs);
        }

        @Override
        public void fulfill(Transaction transaction, int... inputGroup) {
            byte[] lock = aggregateSignatures(rule, signatures);

            List<byte[]> witnesses = transaction.getWitnesses();
            int firstIndex = inputGroup[0];
            byte[] witness = setWitnessArgsLock(witnesses.get(firstIndex), lock);
            witnesses.set(firstIndex, witness);
        }

        public byte[] aggregateSignatures(Rule rule, byte[]... signatures) {
            if (signatures == null || signatures.length != rule.getThreshold()) {
                throw new IllegalArgumentException("Number of signatures must be equal to multisig threshold");
            }
            byte[] aggregatedSignature = rule.getWitnessLockPlaceHolder();
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
        private final Rule rule;
        private final ECKeyPair keyPair;

        protected Signer(Rule rule, ECKeyPair keyPair) {
            this.rule = rule;
            this.keyPair = keyPair;
        }

        public byte[] sign(Transaction transaction, int... inputGroup) {
            byte[] originalWitness = transaction.getWitness((inputGroup[0]));
            byte[] witnessPlaceholder = setWitnessArgsLock(originalWitness, rule.getWitnessLockPlaceHolder());
            Secp256k1Blake160SighashAll.Signer signer = new Secp256k1Blake160SighashAll.Signer(keyPair);
            return signer.sign(transaction, witnessPlaceholder, inputGroup);
        }
    }
}
