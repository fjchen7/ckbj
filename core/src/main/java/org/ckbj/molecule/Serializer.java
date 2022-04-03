package org.ckbj.molecule;

import org.ckbj.type.*;

public class Serializer {
    public static byte[] serialize(CellDep in) {
        return MoleculeFactory.createCellDep(in).toByteArray();
    }

    public static byte[] serialize(CellInput in) {
        return MoleculeFactory.createCellInput(in).toByteArray();
    }

    public static byte[] serialize(Cell in) {
        return MoleculeFactory.createCellOutput(in).toByteArray();
    }

    public static byte[] serialize(Transaction in, boolean includeWitnesses) {
        if (includeWitnesses) {
            return MoleculeFactory.createTransaction(in).toByteArray();
        } else {
            return MoleculeFactory.createRawTransaction(in).toByteArray();
        }
    }

    public static byte[] serialize(Header in, boolean includeNonce) {
        if (includeNonce) {
            return MoleculeFactory.createHeader(in).toByteArray();
        } else {
            return MoleculeFactory.createRawHeader(in).toByteArray();
        }
    }

    public static byte[] serialize(WitnessArgs in) {
        return MoleculeFactory.createWitnessArgs(in).toByteArray();
    }
}
