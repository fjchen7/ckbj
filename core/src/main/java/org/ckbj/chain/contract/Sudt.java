package org.ckbj.chain.contract;

import org.ckbj.chain.Contract;
import org.ckbj.chain.ScriptArgs;
import org.ckbj.chain.address.Address;
import org.ckbj.molecule.Serializer;
import org.ckbj.molecule.type.concrete.Uint128;
import org.ckbj.type.Script;
import org.ckbj.utils.Hex;

import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Sudt {
    public static BigInteger dataToAmount(byte[] data) {
        if (data.length != Uint128.SIZE) {
            throw new IllegalArgumentException("data length must be " + Uint128.SIZE);
        }
        return Hex.toBigInteger(data, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] amountToData(BigInteger amount) {
        byte[] data = Serializer.serialize(amount, Serializer.MoleculeNumber.UINT128);
        return data;
    }

    public static byte[] amountToData(long amount) {
        return amountToData(BigInteger.valueOf(amount));
    }

    public static class Args implements ScriptArgs {
        byte[] args;

        public Args(byte[] args) {
            this.args = args;
        }

        public Args(Script ownerLock) {
            this(ownerLock.hash());

        }

        public Args(Address owner) {
            this(owner.getScript());
        }

        public Args(String owner) {
            this(Address.decode(owner));
        }

        public boolean isOwner(Address address) {
            byte[] args = address.getScript().hash();
            return Arrays.equals(this.args, args);
        }

        public boolean isOwner(String address) {
            return isOwner(Address.decode(address));
        }

        @Override
        public byte[] getArgs() {
            return args;
        }

        @Override
        public Contract.Name getContractName() {
            return Contract.Name.SUDT;
        }
    }
}
