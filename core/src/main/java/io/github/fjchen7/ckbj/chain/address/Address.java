package io.github.fjchen7.ckbj.chain.address;

import io.github.fjchen7.ckbj.type.Script;
import io.github.fjchen7.ckbj.chain.Contract;
import io.github.fjchen7.ckbj.chain.Network;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Objects;

import static io.github.fjchen7.ckbj.chain.Contract.Name.*;

public class Address {
    Script script;
    Network network;

    public Address(Script script, Network network) {
        this.script = script;
        this.network = network;
    }

    public Script getScript() {
        return script;
    }

    public Address setScript(Script script) {
        Objects.requireNonNull(script);
        this.script = script;
        return this;
    }

    public Network getNetwork() {
        return network;
    }

    public Address setNetwork(Network network) {
        Objects.requireNonNull(network);
        this.network = network;
        return this;
    }

    public static Address decode(String address) {
        Objects.requireNonNull(address);
        Bech32.Bech32Data bech32Data = Bech32.decode(address);
        Bech32.Encoding encoding = bech32Data.encoding;
        Network network = network(bech32Data.hrp);
        byte[] payload = bech32Data.data;
        payload = convertBits(payload, 0, payload.length, 5, 8, false);
        Format format = encodeFormat(payload[0]);
        if (format.getEncoding() != encoding) {
            throw new AddressFormatException("Payload header 0x%02x should have encoding %s, but find %s", payload[0], format.getEncoding(), encoding);
        }
        switch (format) {
            case SHORT:
                return decodeShort(payload, network);
            case FULL_BECH32:
                return decodeFullBech32(payload, network);
            case FULL_BECH32M:
                return decodeFullBech32m(payload, network);
            default:
                return null;
        }
    }

    public static Format encodedFormat(String address) {
        Objects.requireNonNull(address);
        Bech32.Bech32Data bech32Data = Bech32.decode(address);
        Bech32.Encoding encoding = bech32Data.encoding;
        byte[] payload = bech32Data.data;
        payload = convertBits(payload, 0, payload.length, 5, 8, false);
        Format format = encodeFormat(payload[0]);
        if (format.getEncoding() != encoding) {
            throw new AddressFormatException("Payload header 0x%02x should have encoding %s, but find %s", payload[0], format.getEncoding(), encoding);
        }
        return format;
    }

    private static Format encodeFormat(byte header) {
        switch (header) {
            case 0x00:
                return Format.FULL_BECH32M;
            case 0x01:
                return Format.SHORT;
            case 0x02:
            case 0x04:
                return Format.FULL_BECH32;
            default:
                throw new AddressFormatException("Unsupported payload header 0x%02x", header);
        }
    }

    private static Address decodeShort(byte[] payload, Network network) {
        if (payload[0] != 0x01) {
            throw new AddressFormatException("Invalid payload header 0x%02x", payload[0]);
        }
        Contract contract;
        byte codeHashIndex = payload[1];
        byte[] args = Arrays.copyOfRange(payload, 2, payload.length);
        if (codeHashIndex == 0x00) {
            if (args.length != 20) {
                throw new AddressFormatException("Invalid args length %d", args.length);
            }
            contract = network.getContract(SECP256K1_BLAKE160_SIGHASH_ALL);
        } else if (codeHashIndex == 0x01) {
            if (args.length != 20) {
                throw new AddressFormatException("Invalid args length %d", args.length);
            }
            contract = network.getContract(SECP256K1_BLAKE160_MULTISIG_ALL);
        } else if (codeHashIndex == 0x02) {
            // https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0026-anyone-can-pay/0026-anyone-can-pay.md
            if (args.length < 20 || args.length > 22) {
                throw new AddressFormatException("Invalid args length %d", args.length);
            }
            contract = network.getContract(ANYONE_CAN_PAY);
        } else {
            throw new AddressFormatException("Invalid code hash index 0x%02x", codeHashIndex);
        }
        Script script = new Script();
        script.setCodeHash(contract.getCodeHash());
        script.setArgs(args);
        script.setHashType(Script.HashType.TYPE);
        return new Address(script, network);
    }

    private static Address decodeFullBech32(byte[] payload, Network network) {
        Script.HashType hashType;
        if (payload[0] == 0x04) {
            hashType = Script.HashType.TYPE;
        } else if (payload[0] == 0x02) {
            hashType = Script.HashType.DATA;
        } else {
            throw new AddressFormatException("Invalid payload header 0x%02x", payload[0]);
        }
        byte[] codeHash = Arrays.copyOfRange(payload, 1, 33);
        byte[] args = Arrays.copyOfRange(payload, 33, payload.length);
        Script script = new Script();
        script.setCodeHash(codeHash);
        script.setArgs(args);
        script.setHashType(hashType);
        return new Address(script, network);
    }

    private static Address decodeFullBech32m(byte[] payload, Network network) {
        if (payload[0] != 0x00) {
            throw new AddressFormatException("Invalid payload header 0x%02x", payload[0]);
        }
        byte[] codeHash = Arrays.copyOfRange(payload, 1, 33);
        Script.HashType hashType = Script.HashType.valueOf(payload[33]);
        byte[] args = Arrays.copyOfRange(payload, 34, payload.length);
        Script script = new Script();
        script.setCodeHash(codeHash);
        script.setArgs(args);
        script.setHashType(hashType);
        return new Address(script, network);
    }

    @Override
    public String toString() {
        return encode();
    }

    public String encode() {
        return encode(Format.FULL_BECH32M);
    }

    public String encode(Format format) {
        Objects.requireNonNull(format);
        switch (format) {
            case SHORT:
                return encodeShort();
            case FULL_BECH32:
                return encodeFullBech32();
            case FULL_BECH32M:
                return encodeFullBech32m();
            default:
                throw new AddressFormatException("Invalid format %s", format);
        }
    }

    private String encodeShort() {
        byte[] payload = new byte[2 + script.getArgs().length];
        byte codeHashIndex;
        Contract.Name contractName = network.getContractName(script);
        if (contractName == SECP256K1_BLAKE160_SIGHASH_ALL) {
            codeHashIndex = 0x00;
        } else if (contractName == SECP256K1_BLAKE160_MULTISIG_ALL) {
            codeHashIndex = 0x01;
        } else if (contractName == ANYONE_CAN_PAY) {
            codeHashIndex = 0x02;
        } else {
            throw new UnsupportedOperationException("Unsupported contract type " + contractName);
        }
        payload[0] = 0x01;
        payload[1] = codeHashIndex;
        System.arraycopy(script.getArgs(), 0, payload, 2, script.getArgs().length);
        payload = convertBits(payload, 0, payload.length, 8, 5, true);
        return Bech32.encode(Bech32.Encoding.BECH32, hrp(network), payload);
    }

    private String encodeFullBech32() {
        byte[] payload = new byte[1 + script.getCodeHash().length + script.getArgs().length];
        if (script.getHashType() == Script.HashType.TYPE) {
            payload[0] = 0x04;
        } else if (script.getHashType() == Script.HashType.DATA) {
            payload[1] = 0x02;
        } else {
            throw new UnsupportedOperationException("Unsupported script hash type " + script.getHashType());
        }
        int pos = 1;
        System.arraycopy(script.getCodeHash(), 0, payload, pos, script.getCodeHash().length);
        pos += script.getCodeHash().length;
        System.arraycopy(script.getArgs(), 0, payload, pos, script.getArgs().length);
        payload = convertBits(payload, 0, payload.length, 8, 5, true);
        return Bech32.encode(Bech32.Encoding.BECH32, hrp(network), payload);
    }

    private String encodeFullBech32m() {
        byte[] payload = new byte[1 + script.getCodeHash().length + 1 + script.getArgs().length];
        payload[0] = 0x00;
        int pos = 1;
        System.arraycopy(script.getCodeHash(), 0, payload, pos, script.getCodeHash().length);
        pos += script.getCodeHash().length;
        payload[pos] = script.getHashType().toByte();
        pos++;
        System.arraycopy(script.getArgs(), 0, payload, pos, script.getArgs().length);
        payload = convertBits(payload, 0, payload.length, 8, 5, true);
        return Bech32.encode(Bech32.Encoding.BECH32M, hrp(network), payload);
    }

    private static String hrp(Network network) {
        Objects.requireNonNull(network);
        switch (network) {
            case MAINNET:
                return "ckb";
            case TESTNET:
                return "ckt";
            default:
                throw new AddressFormatException("Unsupported network %s", network);
        }
    }

    private static Network network(String hrp) {
        switch (hrp) {
            case "ckb":
                return Network.MAINNET;
            case "ckt":
                return Network.TESTNET;
            default:
                throw new AddressFormatException("Invalid hrp %s", hrp);
        }
    }

    private static byte[] convertBits(final byte[] in, final int inStart, final int inLen, final int fromBits,
                                      final int toBits, final boolean pad) throws AddressFormatException {
        int acc = 0;
        int bits = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream(64);
        final int maxv = (1 << toBits) - 1;
        final int max_acc = (1 << (fromBits + toBits - 1)) - 1;
        for (int i = 0; i < inLen; i++) {
            int value = in[i + inStart] & 0xff;
            if ((value >>> fromBits) != 0) {
                throw new AddressFormatException("Input value '%X' exceeds '%d' bit size", value, fromBits);
            }
            acc = ((acc << fromBits) | value) & max_acc;
            bits += fromBits;
            while (bits >= toBits) {
                bits -= toBits;
                out.write((acc >>> bits) & maxv);
            }
        }
        if (pad) {
            if (bits > 0)
                out.write((acc << (toBits - bits)) & maxv);
        } else if (bits >= fromBits || ((acc << (toBits - bits)) & maxv) != 0) {
            throw new AddressFormatException("Could not convert bits, invalid padding");
        }
        return out.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!script.equals(address.script)) return false;
        return network == address.network;
    }

    @Override
    public int hashCode() {
        int result = script.hashCode();
        result = 31 * result + network.hashCode();
        return result;
    }

    public enum Format {
        @Deprecated
        SHORT(Bech32.Encoding.BECH32),
        @Deprecated
        FULL_BECH32(Bech32.Encoding.BECH32),
        FULL_BECH32M(Bech32.Encoding.BECH32M);

        private Bech32.Encoding encoding;

        Format(Bech32.Encoding encoding) {
            this.encoding = encoding;
        }

        public Bech32.Encoding getEncoding() {
            return encoding;
        }
    }
}
