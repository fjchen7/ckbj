package io.github.fjchen7.ckbj.molecule;

import io.github.fjchen7.ckbj.molecule.type.base.Molecule;
import io.github.fjchen7.ckbj.type.*;

import java.math.BigInteger;

public class Serializer {
    public static byte[] serialize(CellDep in) {
        return Converter.toCellDep(in).toByteArray();
    }

    public static byte[] serialize(CellInput in) {
        return Converter.toCellInput(in).toByteArray();
    }

    public static byte[] serialize(Cell in) {
        return Converter.toCellOutput(in).toByteArray();
    }

    public static byte[] serialize(Script in) {
        return Converter.toScript(in).toByteArray();
    }

    public static byte[] serialize(Transaction in, boolean includeWitnesses) {
        if (includeWitnesses) {
            return Converter.toTransaction(in).toByteArray();
        } else {
            return Converter.toRawTransaction(in).toByteArray();
        }
    }

    public static byte[] serialize(Header in, boolean includeNonce) {
        if (includeNonce) {
            return Converter.toHeader(in).toByteArray();
        } else {
            return Converter.toRawHeader(in).toByteArray();
        }
    }

    public static byte[] serialize(io.github.fjchen7.ckbj.type.WitnessArgs in) {
        return Converter.toWitnessArgs(in).toByteArray();
    }

    public static io.github.fjchen7.ckbj.type.WitnessArgs deserializeWitnessArgs(byte[] in) {
        io.github.fjchen7.ckbj.molecule.type.concrete.WitnessArgs molecule = io.github.fjchen7.ckbj.molecule.type.concrete.WitnessArgs.builder(in).build();
        return Converter.fromWitnessArgs(molecule);
    }

    public static byte[] serialize(long in, MoleculeNumber type) {
        return serialize(BigInteger.valueOf(in), type);
    }

    public static byte[] serialize(BigInteger in, MoleculeNumber type) {
        Molecule molecule;
        switch (type) {
            case UINT32:
                molecule = Converter.toUint32(in);
                break;
            case UINT64:
                molecule = Converter.toUint64(in);
                break;
            case UINT128:
                molecule = Converter.toUnit128(in);
                break;
            default:
                throw new IllegalArgumentException("Unsupported molecule number type");
        }
        return molecule.toByteArray();
    }

    public enum MoleculeNumber {
        UINT32,
        UINT64,
        UINT128
    }
}
