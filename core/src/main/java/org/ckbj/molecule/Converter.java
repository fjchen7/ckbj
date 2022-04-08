package org.ckbj.molecule;

import org.ckbj.molecule.type.concrete.*;
import org.ckbj.utils.Hex;

import java.math.BigInteger;
import java.util.List;

class Converter {
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

    protected static Uint32 toUnit32(long in) {
        byte[] arr = Hex.toByteArray(BigInteger.valueOf(in), Uint32.SIZE);
        return Uint32.builder()
                .set(flip(arr))
                .build();
    }

    protected static Uint64 toUnit64(long in) {
        return toUnit64(BigInteger.valueOf(in));
    }

    protected static Uint64 toUnit64(BigInteger in) {
        byte[] arr = Hex.toByteArray(in, Uint64.SIZE);
        return Uint64.builder()
                .set(flip(arr))
                .build();
    }

    protected static Uint64 toUnit64(byte[] in) {
        return Uint64.builder()
                .set(padAfter(flip(in), Uint64.SIZE))
                .build();
    }

    protected static Uint128 toUnit128(BigInteger in) {
        byte[] arr = Hex.toByteArray(in, Uint128.SIZE);
        return Uint128.builder()
                .set(flip(arr))
                .build();
    }

    protected static Byte32 toByte32(byte[] in) {
        return Byte32.builder(in).build();
    }

    protected static Bytes toBytes(byte[] in) {
        if (in == null) {
            return null;
        }
        return Bytes.builder()
                .add(in)
                .build();
    }

    protected static BytesVec toBytesVec(List<byte[]> in) {
        Bytes[] arr = new Bytes[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = toBytes(in.get(i));
        }
        return BytesVec.builder()
                .add(arr)
                .build();
    }

    protected static Byte32Vec toByte32Vec(List<byte[]> in) {
        Byte32[] arr = new Byte32[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = toByte32(in.get(i));
        }
        return Byte32Vec.builder()
                .add(arr)
                .build();
    }

    protected static OutPoint toOutPoint(org.ckbj.type.OutPoint in) {
        return OutPoint.builder()
                .setIndex(toUnit32(in.getIndex()))
                .setTxHash(toByte32(in.getTxHash()))
                .build();
    }

    protected static CellInput toCellInput(org.ckbj.type.CellInput in) {
        return CellInput.builder()
                .setSince(toUnit64(in.getSince()))
                .setPreviousOutput(toOutPoint(in.getPreviousOutput()))
                .build();
    }

    protected static CellInputVec toCellInputVec(List<org.ckbj.type.CellInput> in) {
        CellInput[] arr = new CellInput[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = toCellInput(in.get(i));
        }
        return CellInputVec.builder()
                .add(arr)
                .build();
    }

    protected static CellOutput toCellOutput(org.ckbj.type.Cell in) {
        return CellOutput.builder()
                .setLock(toScript(in.getLock()))
                .setType(toScript(in.getType()))
                .setCapacity(toUnit64(in.getCapacity()))
                .build();
    }

    protected static CellOutputVec toCellOutputVec(List<org.ckbj.type.Cell> in) {
        CellOutput[] arr = new CellOutput[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = toCellOutput(in.get(i));
        }
        return CellOutputVec.builder()
                .add(arr)
                .build();
    }

    protected static CellDep toCellDep(org.ckbj.type.CellDep in) {
        return CellDep.builder()
                .setOutPoint(toOutPoint(in.getOutPoint()))
                .setDepType(in.getDepType().toByte())
                .build();
    }

    protected static CellDepVec toCellDepVec(List<org.ckbj.type.CellDep> in) {
        CellDep[] arr = new CellDep[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = toCellDep(in.get(i));
        }
        return CellDepVec.builder()
                .add(arr)
                .build();
    }

    protected static Script toScript(org.ckbj.type.Script in) {
        if (in == null) {
            return null;
        }
        return Script.builder()
                .setCodeHash(toByte32(in.getCodeHash()))
                .setArgs(toBytes(in.getArgs()))
                .setHashType(in.getHashType().toByte())
                .build();
    }

    protected static RawTransaction toRawTransaction(org.ckbj.type.Transaction in) {
        return RawTransaction.builder()
                .setVersion(toUnit32(in.getVersion()))
                .setCellDeps(toCellDepVec(in.getCellDeps()))
                .setHeaderDeps(toByte32Vec(in.getHeaderDeps()))
                .setInputs(toCellInputVec(in.getInputs()))
                .setOutputs(toCellOutputVec(in.getOutputs()))
                .setOutputsData(toBytesVec(in.getOutputsData()))
                .build();
    }

    protected static Transaction toTransaction(org.ckbj.type.Transaction in) {
        return Transaction.builder()
                .setRaw(toRawTransaction(in))
                .setWitnesses(toBytesVec(in.getWitnesses()))
                .build();
    }

    protected static RawHeader toRawHeader(org.ckbj.type.Header in) {
        RawHeader header = RawHeader.builder()
                .setVersion(toUnit32(in.getVersion()))
                .setCompactTarget(toUnit32(in.getCompactTarget()))
                .setTimestamp(toUnit64(in.getTimestamp()))
                .setNumber(toUnit64(in.getNumber()))
                .setEpoch(toUnit64(in.getEpoch().toByteArray()))
                .setParentHash(toByte32(in.getParentHash()))
                .setTransactionsRoot(toByte32(in.getTransactionsRoot()))
                .setProposalsHash(toByte32(in.getProposalsHash()))
                .setExtraHash(toByte32(in.getExtraHash()))
                .setDao(toByte32(in.getDao()))
                .build();
        return header;
    }

    protected static Header toHeader(org.ckbj.type.Header in) {
        return Header.builder()
                .setRaw(toRawHeader(in))
                .setNonce(toUnit128(in.getNonce()))
                .build();
    }

    protected static WitnessArgs toWitnessArgs(org.ckbj.type.WitnessArgs in) {
        return WitnessArgs.builder()
                .setLock(toBytes(in.getLock()))
                .setInputType(toBytes(in.getInputType()))
                .setOutputType(toBytes(in.getOutputType()))
                .build();
    }

    protected static org.ckbj.type.WitnessArgs fromWitnessArgs(WitnessArgs in) {
        return new org.ckbj.type.WitnessArgs()
                .setLock(in.getLock() == null ? null : in.getLock().getItems())
                .setInputType(in.getInputType() == null ? null : in.getInputType().getItems())
                .setOutputType(in.getOutputType() == null ? null : in.getOutputType().getItems());
    }
}
