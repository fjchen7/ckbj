package org.ckbj.molecule;

import org.ckbj.molecule.type.concrete.*;

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
}
