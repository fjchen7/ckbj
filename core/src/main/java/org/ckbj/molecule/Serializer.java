package org.ckbj.molecule;

import org.ckbj.molecule.type.base.Molecule;
import org.ckbj.molecule.type.concrete.WitnessArgs;

import java.math.BigInteger;

public class Serializer {
    public static byte[] serialize(org.ckbj.type.CellDep in) {
        return Converter.toCellDep(in).toByteArray();
    }

    public static byte[] serialize(org.ckbj.type.CellInput in) {
        return Converter.toCellInput(in).toByteArray();
    }

    public static byte[] serialize(org.ckbj.type.Cell in) {
        return Converter.toCellOutput(in).toByteArray();
    }

    public static byte[] serialize(org.ckbj.type.Script in) {
        return Converter.toScript(in).toByteArray();
    }

    public static byte[] serialize(org.ckbj.type.Transaction in, boolean includeWitnesses) {
        if (includeWitnesses) {
            return Converter.toTransaction(in).toByteArray();
        } else {
            return Converter.toRawTransaction(in).toByteArray();
        }
    }

    public static byte[] serialize(org.ckbj.type.Header in, boolean includeNonce) {
        if (includeNonce) {
            return Converter.toHeader(in).toByteArray();
        } else {
            return Converter.toRawHeader(in).toByteArray();
        }
    }

    public static byte[] serialize(org.ckbj.type.WitnessArgs in) {
        return Converter.toWitnessArgs(in).toByteArray();
    }

    public static org.ckbj.type.WitnessArgs deserializeWitnessArgs(byte[] in) {
        WitnessArgs molecule = WitnessArgs.builder(in).build();
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
