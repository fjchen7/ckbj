package org.ckbj.chain.sign;

import org.ckbj.chain.Contract;
import org.ckbj.crypto.Blake2b;
import org.ckbj.crypto.ECKeyPair;
import org.ckbj.crypto.Sign;
import org.ckbj.molecule.Serializer;
import org.ckbj.type.Cell;
import org.ckbj.type.Transaction;
import org.ckbj.type.WitnessArgs;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class Secp256K1Blake160SighashAllSigner extends StandardLockScriptSigner {
    private final ECKeyPair keyPair;

    public Secp256K1Blake160SighashAllSigner(ECKeyPair keyPair) {
        super();
        this.keyPair = keyPair;
    }

    public Secp256K1Blake160SighashAllSigner(BigInteger privateKey) {
        this(ECKeyPair.create(privateKey));
    }

    @Override
    public Contract.Standard getContractName() {
        return Contract.Standard.SECP256K1_BLAKE160_SIGHASH_ALL;
    }

    @Override
    protected boolean doMatch(byte[] scriptArgs) {
        byte[] hash = Blake2b.digest(keyPair.getEncodedPublicKey(true));
        hash = Arrays.copyOfRange(hash, 0, 20);
        return Arrays.equals(scriptArgs, hash);
    }

    @Override
    protected boolean doSign(Transaction transaction, List<Cell> inputsDetail, List<Integer> inputGroup) {
        if (inputGroup.size() == 0) {
            return false;
        }
        byte[] txHash = transaction.hash();
        List<byte[]> witnesses = transaction.getWitnesses();
        Blake2b blake2b = new Blake2b();
        blake2b.update(txHash);
        for (int i : inputGroup) {
            byte[] witness = witnesses.get(i);
            blake2b.update(Serializer.serialize(witness.length, Serializer.MoleculeNumber.UINT64));
            blake2b.update(witness);
        }
        for (int i = transaction.getInputs().size(); i < transaction.getWitnesses().size(); i++) {
            byte[] witness = witnesses.get(i);
            blake2b.update(Serializer.serialize(witness.length, Serializer.MoleculeNumber.UINT64));
            blake2b.update(witness);
        }
        byte[] digest = blake2b.doFinal();
        byte[] signature = Sign.signMessage(digest, keyPair, false).getSignature();

        int index = inputGroup.get(0);
        WitnessArgs witnessArgs = Serializer.deserializeWitnessArgs(witnesses.get(index));
        witnessArgs.setLock(signature);
        witnesses.set(index, Serializer.serialize(witnessArgs));
        return true;
    }
}