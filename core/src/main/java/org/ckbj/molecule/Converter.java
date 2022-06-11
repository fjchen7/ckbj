package org.ckbj.molecule;

import org.ckbj.molecule.type.concrete.*;
import org.ckbj.utils.Hex;

import java.math.BigInteger;
import java.util.List;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

class Converter {
    protected static Uint32 toUint32(BigInteger in) {
        byte[] arr = Hex.toByteArray(in, Uint32.SIZE, LITTLE_ENDIAN);
        return Uint32.builder()
                .set(arr)
                .build();
    }

    protected static Uint32 toUint32(long in) {
        return toUint32(BigInteger.valueOf(in));
    }

    protected static Uint64 toUint64(BigInteger in) {
        byte[] arr = Hex.toByteArray(in, Uint64.SIZE, LITTLE_ENDIAN);
        return Uint64.builder()
                .set(arr)
                .build();
    }

    protected static Uint64 toUint64(long in) {
        return toUint64(BigInteger.valueOf(in));
    }

    protected static Uint64 toUint64(byte[] in) {
        return toUint64(Hex.toBigInteger(in));
    }

    protected static Uint128 toUnit128(BigInteger in) {
        byte[] arr = Hex.toByteArray(in, Uint128.SIZE, LITTLE_ENDIAN);
        return Uint128.builder()
                .set(arr)
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
                .setIndex(toUint32(in.getIndex()))
                .setTxHash(toByte32(in.getTxHash()))
                .build();
    }

    protected static CellInput toCellInput(org.ckbj.type.CellInput in) {
        return CellInput.builder()
                .setSince(toUint64(in.getSince()))
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
                .setCapacity(toUint64(in.getCapacity()))
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
                .setVersion(toUint32(in.getVersion()))
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
                .setVersion(toUint32(in.getVersion()))
                .setCompactTarget(toUint32(in.getCompactTarget()))
                .setTimestamp(toUint64(in.getTimestamp()))
                .setNumber(toUint64(in.getNumber()))
                .setEpoch(toUint64(in.getEpoch().toByteArray()))
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
        org.ckbj.type.WitnessArgs witnessArgs = new org.ckbj.type.WitnessArgs();
        witnessArgs.setLock(in.getLock() == null ? null : in.getLock().getItems());
        witnessArgs.setInputType(in.getInputType() == null ? null : in.getInputType().getItems());
        witnessArgs.setOutputType(in.getOutputType() == null ? null : in.getOutputType().getItems());
        return witnessArgs;
    }
}
