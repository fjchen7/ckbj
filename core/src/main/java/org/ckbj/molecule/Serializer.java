package org.ckbj.molecule;

import org.ckbj.type.*;

public class Serializer {
    public static byte[] serialize(CellDep in) {
        return MoleculeFactory.createCellDep(in).getRawData();
    }

    public static byte[] serialize(CellInput in) {
        return MoleculeFactory.createCellInput(in).getRawData();
    }

    public static byte[] serialize(Cell in) {
        return MoleculeFactory.createCellOutput(in).getRawData();
    }

    public static byte[] serialize(Transaction in, boolean includeWitnesses) {
        if (includeWitnesses) {
            return MoleculeFactory.createTransaction(in).getRawData();
        } else {
            return MoleculeFactory.createRawTransaction(in).getRawData();
        }
    }

    public static byte[] serialize(WitnessArgs in) {
        return MoleculeFactory.createWitnessArgs(in).getRawData();
    }
}
