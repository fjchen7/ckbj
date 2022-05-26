package org.ckbj.chain;

import org.ckbj.chain.address.Address;
import org.ckbj.molecule.Serializer;
import org.ckbj.type.Script;
import org.ckbj.type.WitnessArgs;

public interface StandardLockContractArgs extends ContractArgs {
    byte[] getArgs();

    Contract.Type getContractType();

    default Address toAddress(Network network) {
        Contract.Type contractType = getContractType();
        byte[] args = getArgs();
        Script script = network.getContract(contractType).createScript(args);
        return new Address(script, network);
    }

    byte[] getWitnessPlaceholder(byte[] originalWitness);

    static byte[] setWitnessArgsLock(byte[] originalWitness, byte[] lockPlaceholder) {
        WitnessArgs witnessArgs;
        if (originalWitness == null || originalWitness.length == 0) {
            witnessArgs = WitnessArgs
                    .builder()
                    .build();
        } else {
            witnessArgs = Serializer.deserializeWitnessArgs(originalWitness);
        }
        witnessArgs.setLock(lockPlaceholder);
        return Serializer.serialize(witnessArgs);
    }
}
