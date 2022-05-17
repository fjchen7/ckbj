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
import org.ckbj.type.WitnessArgs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ckbj.chain.contract.StandardLockScriptFulfillment.getWitnessPlaceHolder;

public class Secp256k1Blake160MultisigAll {
    public static byte[] createArgs(int threshold, byte[]... publicKeyHashes) {
        Rule rule = Rule.builder()
                .setR(0)
                .setM(threshold)
                .setPublicKeyHashes(Arrays.asList(publicKeyHashes))
                .build();
        return createArgs(rule);
    }

    public static byte[] createArgs(Rule rule) {
        byte[] hash = Blake2b.digest(rule.encode());
        return Arrays.copyOfRange(hash, 0, 20);
    }

    public static byte[] aggregatedSignaturesPlaceholder(Rule rule) {
        byte[] multisigScript = rule.encode();
        byte[] lockPlaceholder = new byte[multisigScript.length + rule.getM() * Sign.SIGNATURE_LENGTH];
        System.arraycopy(multisigScript, 0, lockPlaceholder, 0, multisigScript.length);
        return lockPlaceholder;
    }

    public static Fulfillment fulfillment(Rule rule, byte[]... signatures) {
        return new Fulfillment(rule, signatures);
    }

    /**
     * https://github.com/nervosnetwork/ckb-system-scripts/wiki/How-to-sign-transaction#multisig
     */
    public static class Rule {
        private byte s = 0x0;
        private int r;
        private int m;
        private List<byte[]> publicKeyHashes;

        /**
         * The format version
         */
        public byte getS() {
            return s;
        }

        /**
         * The first R items in public key list whose signatures must be provided for.
         */
        public int getR() {
            return r;
        }

        /**
         * signature threshold
         */
        public int getM() {
            return m;
        }

        /**
         * Size of public key list
         */
        public int getN() {
            return publicKeyHashes.size();
        }

        public List<byte[]> getPublicKeyHashes() {
            return publicKeyHashes;
        }

        public static Builder builder() {
            return new Builder();
        }

        public byte[] encode() {
            byte[] out = new byte[4 + this.publicKeyHashes.size() * 20];
            out[0] = this.s;
            out[1] = (byte) (this.r & 0xff);
            out[2] = (byte) (this.m & 0xff);
            out[3] = (byte) (this.publicKeyHashes.size() & 0xff);
            int pos = 4;
            for (byte[] publicKeyHash: this.publicKeyHashes) {
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
            rule.publicKeyHashes = new ArrayList<>();
            for (int i = 0; i < in[3]; i++) {
                byte[] publicKeyHash = new byte[20];
                System.arraycopy(in, 4 + i * 20, publicKeyHash, 0, 20);
                rule.publicKeyHashes.add(publicKeyHash);
            }
            return rule;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Rule rule = (Rule) o;

            if (s != rule.s) return false;
            if (r != rule.r) return false;
            if (m != rule.m) return false;

            if (publicKeyHashes.size() != rule.publicKeyHashes.size()) return false;
            for (int i = 0; i < publicKeyHashes.size(); i++) {
                if (!Arrays.equals(publicKeyHashes.get(i), rule.publicKeyHashes.get(i))) {
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
            result = 31 * result + publicKeyHashes.hashCode();
            return result;
        }

        public static final class Builder {
            private byte s = 0x0;
            private int r = 0;
            private int m;
            private List<byte[]> publicKeyHashes = new ArrayList<>();

            private Builder() {
            }

            public Builder setS(byte s) {
                this.s = s;
                return this;
            }

            /**
             * The first R items in public key list whose signatures must be provided for.
             */
            public Builder setR(int r) {
                if (r < 0) {
                    throw new IllegalArgumentException("firstN must be greater than or equal to 0");
                }
                this.r = r;
                return this;
            }

            /**
             * signature threshold
             */
            public Builder setM(int m) {
                this.m = m;
                return this;
            }

            public Builder setPublicKeyHashes(List<byte[]> publicKeyHashes) {
                for (byte[] publicKeyHash: publicKeyHashes) {
                    if (publicKeyHash.length != 20) {
                        throw new IllegalArgumentException("public key hash must be 20 bytes");
                    }
                }
                this.publicKeyHashes = publicKeyHashes;
                return this;
            }

            public Builder addPublicKeyHash(byte[] publicKeyHash) {
                if (publicKeyHash.length != 20) {
                    throw new IllegalArgumentException("public key hash must be 20 bytes");
                }
                publicKeyHashes.add(publicKeyHash);
                return this;
            }

            public Builder addPublicKeyHash(ECKeyPair.Point publicKey) {
                byte[] publicKeyHash = Secp256k1Blake160SighashAll.createArgs(publicKey);
                return addPublicKeyHash(publicKeyHash);
            }

            public Builder addPublicKeyHash(String address) {
                Script script = Address.decode(address).getScript();
                if (Network.TESTNET.getContractType(script) == Contract.Type.SECP256K1_BLAKE160_SIGHASH_ALL
                        || Network.TESTNET.getContractType(script) == Contract.Type.SECP256K1_BLAKE160_SIGHASH_ALL) {
                    return addPublicKeyHash(script.getArgs());
                } else {
                    throw new IllegalArgumentException("input is not a secp256k1Blake160SighashAll address");
                }
            }

            public Rule build() {
                if (publicKeyHashes.size() == 0) {
                    throw new IllegalArgumentException("public key hashes must not be empty");
                }
                if (publicKeyHashes.size() < m) {
                    throw new IllegalArgumentException("size of public key hashes must be greater than or equal to threshold");
                }
                Rule rule = new Rule();
                rule.s = this.s;
                rule.r = this.r;
                rule.m = this.m;
                rule.publicKeyHashes = this.publicKeyHashes;
                return rule;
            }
        }
    }

    public static class Fulfillment extends StandardLockScriptFulfillment {
        private final Rule rule;
        private final byte[][] signatures;

        public Fulfillment(Rule rule, byte[]... signatures) {
            this.rule = rule;
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
            List<byte[]> witnesses = transaction.getWitnesses();
            int firstIndex = inputGroup[0];
            byte[] witness = aggregateSignatures(rule, signatures);
            WitnessArgs witnessArgs = Serializer.deserializeWitnessArgs(witnesses.get(firstIndex));
            witnessArgs.setLock(witness);
            witnesses.set(firstIndex, Serializer.serialize(witnessArgs));
        }

        public byte[] aggregateSignatures(Rule rule, byte[]... signatures) {
            if (signatures == null || signatures.length < rule.getM()) {
                throw new IllegalArgumentException("signatures is null or less than threshold");
            }
            byte[] finalSignature = aggregatedSignaturesPlaceholder(rule);
            int pos = 4 + 20 * signatures.length;
            for (int i = 0; i < signatures.length; i++) {
                byte[] signature = signatures[i];
                if (signature.length != Sign.SIGNATURE_LENGTH) {
                    throw new IllegalArgumentException("signature length is not " + Sign.SIGNATURE_LENGTH);
                }
                System.arraycopy(signature, 0, finalSignature, pos, Sign.SIGNATURE_LENGTH);
                pos += Sign.SIGNATURE_LENGTH;
            }
            return finalSignature;
        }
    }


    public static class Signer {
        private final Rule rule;
        private final ECKeyPair keyPair;

        public Signer(Rule rule, ECKeyPair keyPair) {
            this.rule = rule;
            this.keyPair = keyPair;
        }

        public byte[] sign(Transaction transaction, int... inputGroup) {
            byte[] originalWitness = transaction.getWitness((inputGroup[0]));
            byte[] witnessPlaceholder = getWitnessPlaceHolder(originalWitness, aggregatedSignaturesPlaceholder(rule));

            Secp256k1Blake160SighashAll.Signer signer = new Secp256k1Blake160SighashAll.Signer(keyPair);
            return signer.sign(transaction, witnessPlaceholder, inputGroup);
        }
    }
}
