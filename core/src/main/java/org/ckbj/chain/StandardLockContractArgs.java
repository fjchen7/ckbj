package org.ckbj.chain;

import org.ckbj.chain.address.Address;
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
        WitnessArgs witnessArgs = WitnessArgs.decode(originalWitness);
        witnessArgs.setLock(lockPlaceholder);
        return witnessArgs.encode();
    }
}
