package org.ckbj.chain;

import org.ckbj.type.Script;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultContractCollection implements ContractCollection {

    private Map<Contract.Type, Contract> contractTypeContractMap = new EnumMap<>(Contract.Type.class);
    private Map<Script, Contract.Type> scriptContractTypeMap = new HashMap<>();

    public void register(Contract.Type contractType, Contract contract) {
        Objects.requireNonNull(contractType);
        Objects.requireNonNull(contract);
        contractTypeContractMap.put(contractType, contract);
        Script script = contract.createScript(new byte[0]);
        scriptContractTypeMap.put(script, contractType);
    }

    @Override
    public Contract getContract(Contract.Type contractType) {
        return contractTypeContractMap.get(contractType);
    }

    @Override
    public Contract getContract(Script script) {
        return getContract(getContractType(script));
    }

    @Override
    public Contract.Type getContractType(Script script) {
        if (script == null) {
            return null;
        }
        Script key = Script.builder()
                .setArgs(new byte[0])
                .setCodeHash(script.getCodeHash())
                .setHashType(script.getHashType())
                .build();
        return scriptContractTypeMap.get(key);
    }
}
