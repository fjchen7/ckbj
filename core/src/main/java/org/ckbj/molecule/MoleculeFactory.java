package org.ckbj.molecule;

import org.ckbj.molecule.type.concrete.*;
import org.ckbj.type.Cell;
import org.ckbj.utils.Hex;

import java.math.BigInteger;
import java.util.List;

class MoleculeFactory {
    /**
     * Pad zero after byte array
     */
    private static byte[] padAfter(byte[] in, int length) {
        byte[] padBytes = new byte[length];
        System.arraycopy(in, 0, padBytes, 0, in.length);
        return padBytes;
    }

    private static byte[] flip(byte[] in) {
        byte[] out = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[in.length - i - 1];
        }
        return out;
    }

    protected static Uint32 createUnit32(long in) {
        byte[] arr = Hex.toByteArray(BigInteger.valueOf(in));
        return Uint32
                .builder(padAfter(flip(arr), Uint32.SIZE))
                .build();
    }

    protected static Uint64 createUnit64(long in) {
        return createUnit64(BigInteger.valueOf(in));
    }

    protected static Uint64 createUnit64(BigInteger in) {
        return createUnit64(Hex.toByteArray(in));
    }

    protected static Uint64 createUnit64(byte[] in) {
        return Uint64
                .builder(padAfter(flip(in), Uint64.SIZE))
                .build();
    }

    protected static Uint128 createUnit128(BigInteger in) {
        byte[] arr = Hex.toByteArray(in);
        return Uint128
                .builder(padAfter(flip(arr), Uint128.SIZE))
                .build();
    }

    protected static Byte32 createByte32(byte[] in) {
        return Byte32.builder(in).build();
    }

    protected static Bytes createBytes(byte[] in) {
        if (in == null) {
            return null;
        }
        return Bytes.builder()
                .add(in)
                .build();
    }

    protected static BytesVec createBytesVec(List<byte[]> in) {
        Bytes[] arr = new Bytes[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createBytes(in.get(i));
        }
        return BytesVec.builder()
                .add(arr)
                .build();
    }

    protected static Byte32Vec createByte32Vec(List<byte[]> in) {
        Byte32[] arr = new Byte32[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createByte32(in.get(i));
        }
        return Byte32Vec.builder()
                .add(arr)
                .build();
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
        CellInput[] arr = new CellInput[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createCellInput(in.get(i));
        }
        return CellInputVec.builder()
                .add(arr)
                .build();
    }

    protected static CellOutput createCellOutput(Cell in) {
        return CellOutput.builder()
                .setLock(createScript(in.getLock()))
                .setType(createScript(in.getType()))
                .setCapacity(createUnit64(in.getCapacity()))
                .build();
    }

    protected static CellOutputVec createCellOutputVec(List<Cell> in) {
        CellOutput[] arr = new CellOutput[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createCellOutput(in.get(i));
        }
        return CellOutputVec.builder()
                .add(arr)
                .build();
    }

    protected static CellDep createCellDep(org.ckbj.type.CellDep in) {
        return CellDep.builder()
                .setOutPoint(createOutPoint(in.getOutPoint()))
                .setDepType(in.getDepType().toByte())
                .build();
    }

    protected static CellDepVec createCellDepVec(List<org.ckbj.type.CellDep> in) {
        CellDep[] arr = new CellDep[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createCellDep(in.get(i));
        }
        return CellDepVec.builder()
                .add(arr)
                .build();
    }

    protected static Script createScript(org.ckbj.type.Script in) {
        if (in == null) {
            return null;
        }
        return Script.builder()
                .setCodeHash(createByte32(in.getCodeHash()))
                .setArgs(createBytes(in.getArgs()))
                .setHashType(in.getHashType().toByte())
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

    protected static RawHeader createRawHeader(org.ckbj.type.Header in) {
        RawHeader header = RawHeader.builder()
                .setVersion(createUnit32(in.getVersion()))
                .setCompactTarget(createUnit32(in.getCompactTarget()))
                .setTimestamp(createUnit64(in.getTimestamp()))
                .setNumber(createUnit64(in.getNumber()))
                .setEpoch(createUnit64(in.getEpoch().toByteArray()))
                .setParentHash(createByte32(in.getParentHash()))
                .setTransactionsRoot(createByte32(in.getTransactionsRoot()))
                .setProposalsHash(createByte32(in.getProposalsHash()))
                .setExtraHash(createByte32(in.getExtraHash()))
                .setDao(createByte32(in.getDao()))
                .build();
        return header;
    }

    protected static Header createHeader(org.ckbj.type.Header in) {
        return Header.builder()
                .setRaw(createRawHeader(in))
                .setNonce(createUnit128(in.getNonce()))
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
