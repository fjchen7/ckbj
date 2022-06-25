package io.github.fjchen7.ckbj.chain.contract;

import io.github.fjchen7.ckbj.crypto.Blake2b;
import io.github.fjchen7.ckbj.crypto.ECKeyPair;
import io.github.fjchen7.ckbj.crypto.Sign;
import io.github.fjchen7.ckbj.type.Script;
import io.github.fjchen7.ckbj.type.WitnessArgs;
import io.github.fjchen7.ckbj.chain.Contract;
import io.github.fjchen7.ckbj.chain.LockScriptArgs;
import io.github.fjchen7.ckbj.chain.Network;
import io.github.fjchen7.ckbj.chain.address.Address;

import java.util.Arrays;

public class AnyoneCanPay {
    public static Args.Builder newArgsBuilder() {
        return new Args.Builder();
    }

    // https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0026-anyone-can-pay/0026-anyone-can-pay.md
    public static class Args implements LockScriptArgs {
        private byte[] publicKeyHash;
        private int minimumCapacityExponent;
        private int minimalUDTExponent;

        private Args(byte[] publicKeyHash, int minimumCapacityExponent, int minimalUDTExponent) {
            if (publicKeyHash.length != 20) {
                throw new IllegalArgumentException("publicKeyHash must be 20 bytes");
            }
            if (minimumCapacityExponent < 0 || minimumCapacityExponent > 255) {
                throw new IllegalArgumentException("minimumCapacityExponent must be in range [0, 255]");
            }
            if (minimalUDTExponent < 0 || minimalUDTExponent > 255) {
                throw new IllegalArgumentException("minimalUDTExponent must be in range [0, 255]");
            }
            this.publicKeyHash = publicKeyHash;
            this.minimumCapacityExponent = minimumCapacityExponent;
            this.minimalUDTExponent = minimalUDTExponent;
        }

        public byte[] getPublicKeyHash() {
            return publicKeyHash;
        }

        public int getMinimumCapacityExponent() {
            return minimumCapacityExponent;
        }

        public int getMinimalUDTExponent() {
            return minimalUDTExponent;
        }

        @Override
        public byte[] getArgs() {
            // three kinds of args lengths:
            // <20 byte blake160 public key hash>
            // <20 byte blake160 public key hash> <1 byte CKByte minimum>
            // <20 byte blake160 public key hash> <1 byte CKByte minimum> <1 byte UDT minimum>
            int argsLength = 20;
            if (minimalUDTExponent != 0) {
                argsLength = 22;
            } else if (minimumCapacityExponent != 0) {
                argsLength = 21;
            }

            byte[] args = new byte[argsLength];
            System.arraycopy(publicKeyHash, 0, args, 0, 20);
            if (argsLength == 21) {
                args[20] = (byte) minimumCapacityExponent;
            } else if (argsLength == 22) {
                args[20] = (byte) minimumCapacityExponent;
                args[21] = (byte) minimalUDTExponent;
            }
            return args;
        }

        public static Args decode(byte[] args) {
            if (args.length < 20 || args.length > 22) {
                throw new IllegalArgumentException("args length must be between 20 and 22");
            }
            Builder builder = new Builder();
            builder.setPublicKeyHash(Arrays.copyOfRange(args, 0, 20));
            if (args.length >= 21) {
                builder.setMinimumCapacityExponent(args[21]);
            }
            if (args.length == 22) {
                builder.setMinimalUDTExponent(args[22]);
            }
            return builder.build();
        }

        @Override
        public Contract.Name getContractName() {
            return Contract.Name.ANYONE_CAN_PAY;
        }

        @Override
        public byte[] getWitnessPlaceholder(byte[] originalWitness) {
            WitnessArgs witnessArgs = WitnessArgs.decode(originalWitness);
            witnessArgs.setLock(new byte[Sign.SIGNATURE_LENGTH]);
            return witnessArgs.encode();
        }

        public static class Builder {
            private byte[] publicKeyHash;
            private int minimumCapacityExponent = 0;
            private int minimalUDTExponent = 0;

            private Builder() {
            }

            public Builder setPublicKeyHash(byte[] publicKeyHash) {
                if (publicKeyHash.length != 20) {
                    throw new IllegalArgumentException("publicKeyHash must be 20 bytes");
                }
                this.publicKeyHash = publicKeyHash;
                return this;
            }

            public Builder setPublicKeyHash(ECKeyPair publicKey) {
                return setPublicKeyHash(publicKey.getPublicKey());
            }

            public Builder setPublicKeyHash(ECKeyPair.Point publicKey) {
                byte[] publicKeyHash = Blake2b.digest(publicKey.encode(true));
                publicKeyHash = Arrays.copyOfRange(publicKeyHash, 0, 20);
                return setPublicKeyHash(publicKeyHash);
            }

            public Builder setPublicKeyHash(Address secp256k1Blake160SighashAllAddress) {
                Network network = secp256k1Blake160SighashAllAddress.getNetwork();
                Script script = secp256k1Blake160SighashAllAddress.getScript();
                if (network.getContractName(script)
                        != Contract.Name.SECP256K1_BLAKE160_SIGHASH_ALL) {
                    throw new IllegalArgumentException("address must be secp256k1-blake160-sighash-all type");
                }
                return setPublicKeyHash(script.getArgs());
            }

            public Builder setPublicKeyHash(String secp256k1Blake160SighashAllAddress) {
                return setPublicKeyHash(Address.decode(secp256k1Blake160SighashAllAddress));
            }

            public Builder setMinimumCapacityExponent(int minimumCapacityExponent) {
                if (minimumCapacityExponent < 0 || minimumCapacityExponent > 255) {
                    throw new IllegalArgumentException("minimumCapacityExponent must be in range [0, 255]");
                }
                this.minimumCapacityExponent = minimumCapacityExponent;
                return this;
            }

            public Builder setMinimalUDTExponent(int minimalUDTExponent) {
                if (minimalUDTExponent < 0 || minimalUDTExponent > 255) {
                    throw new IllegalArgumentException("minimalUDTExponent must be in range [0, 255]");
                }
                this.minimalUDTExponent = minimalUDTExponent;
                return this;
            }

            public Args build() {
                return new Args(publicKeyHash, minimumCapacityExponent, minimalUDTExponent);
            }
        }
    }
}
