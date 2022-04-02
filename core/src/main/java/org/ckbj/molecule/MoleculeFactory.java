package org.ckbj.molecule;

import org.ckbj.molecule.type.concrete.*;
import org.ckbj.type.Cell;

import java.math.BigInteger;
import java.util.List;

class MoleculeFactory {
    /**
     * Pad zero after byte array
     */
    private static byte[] padBytesAfter(byte[] in, int length) {
        if (in.length > length) {
            throw new IllegalArgumentException("Out of byte size " + length);
        }
        byte[] padBytes = new byte[length];
        System.arraycopy(in, 0, padBytes, 0, in.length);
        return padBytes;
    }

    private static byte[] littleEndian(BigInteger in, int length) {
        byte[] bytes = in.toByteArray();
        // flip bytes
        for (int i = 0; i < bytes.length / 2; i++) {
            byte tmp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = tmp;
        }
        return padBytesAfter(bytes, length);
    }

    protected static Uint32 createUnit32(long in) {
        byte[] bytes = littleEndian(BigInteger.valueOf(in), Uint32.SIZE);
        return Uint32.builder(bytes).build();
    }

    protected static Uint64 createUnit64(BigInteger in) {
        return Uint64
                .builder(littleEndian(in, Uint64.SIZE))
                .build();
    }

    protected static Uint64 createUnit64(byte[] in) {
        return Uint64
                .builder(padBytesAfter(in, Uint64.SIZE))
                .build();
    }

    protected static Byte32 createByte32(byte[] in) {
        return Byte32.builder(in).build();
    }

    protected static Bytes createBytes(byte[] in) {
        if (in == null) {
            return null;
        }
        Bytes.Builder builder = Bytes.builder();
        for (int i = 0; i < in.length; i++) {
            builder.add(in[i]);
        }
        return builder.build();
    }

    protected static BytesVec createBytesVec(List<byte[]> in) {
        BytesVec.Builder builder = BytesVec.builder();
        for (int i = 0; i < in.size(); i++) {
            builder.add(createBytes(in.get(i)));
        }
        return builder.build();
    }

    protected static Byte32Vec createByte32Vec(List<byte[]> in) {
        Byte32Vec.Builder builder = Byte32Vec.builder();
        for (int i = 0; i < in.size(); i++) {
            builder.add(createByte32(in.get(i)));
        }
        return builder.build();
    }

    protected static OutPoint createOutPoint(org.ckbj.type.OutPoint in) {
        return OutPoint.builder()
                .setIndex(createUnit32(in.getIndex()))
                .setTxHash(createByte32(in.getTxHash()))
                .build();
    }

    protected static CellInput createCellInput(org.ckbj.type.CellInput in) {
        return CellInput.builder()
                .setSince(createUnit64(in.getSince()))
                .setPreviousOutput(createOutPoint(in.getPreviousOutput()))
                .build();
    }

    protected static CellInputVec createCellInputVec(List<org.ckbj.type.CellInput> in) {
        CellInputVec.Builder builder = CellInputVec.builder();
        for (int i = 0; i < in.size(); i++) {
            builder.add(createCellInput(in.get(i)));
        }
        return builder.build();
    }

    protected static CellOutput createCellOutput(Cell in) {
        return CellOutput.builder()
                .setLock(createScript(in.getLock()))
                .setType(createScript(in.getType()))
                .setCapacity(createUnit64(in.getCapacity()))
                .build();
    }

    protected static CellOutputVec createCellOutputVec(List<Cell> in) {
        CellOutputVec.Builder builder = CellOutputVec.builder();
        for (int i = 0; i < in.size(); i++) {
            builder.add(createCellOutput(in.get(i)));
        }
        return builder.build();
    }

    protected static CellDep createCellDep(org.ckbj.type.CellDep in) {
        return CellDep.builder()
                .setOutPoint(createOutPoint(in.getOutPoint()))
                .setDepType(in.getDepType().toByte())
                .build();
    }

    protected static CellDepVec createCellDepVec(List<org.ckbj.type.CellDep> in) {
        CellDepVec.Builder builder = CellDepVec.builder();
        for (int i = 0; i < in.size(); i++) {
            builder.add(createCellDep(in.get(i)));
        }
        return builder.build();
    }

    protected static Script createScript(org.ckbj.type.Script in) {
        if (in == null) {
            return null;
        }
        return Script.builder()
                .setCodeHash(createByte32(in.getCodeHash()))
                .setArgs(createBytes(in.getArgs()))
                .setHashType(in.getHashType().value())
                .build();
    }

    protected static RawTransaction createRawTransaction(org.ckbj.type.Transaction in) {
        return RawTransaction.builder()
                .setVersion(createUnit32(in.getVersion()))
                .setCellDeps(createCellDepVec(in.getCellDeps()))
                .setHeaderDeps(createByte32Vec(in.getHeaderDeps()))
                .setInputs(createCellInputVec(in.getInputs()))
                .setOutputs(createCellOutputVec(in.getOutputs()))
                .setOutputsData(createBytesVec(in.getOutputsData()))
                .build();
    }

    protected static Transaction createTransaction(org.ckbj.type.Transaction in) {
        return Transaction.builder()
                .setRaw(createRawTransaction(in))
                .setWitnesses(createBytesVec(in.getWitnesses()))
                .build();
    }

    protected static WitnessArgs createWitnessArgs(org.ckbj.type.WitnessArgs in) {
        return WitnessArgs.builder()
                .setLock(createBytes(in.getLock()))
                .setInputType(createBytes(in.getInputType()))
                .setOutputType(createBytes(in.getOutputType()))
                .build();
    }
}
