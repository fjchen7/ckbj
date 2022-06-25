package io.github.fjchen7.ckbj.chain;

import io.github.fjchen7.ckbj.type.Script;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultContractCollection implements ContractCollection {

    private Map<Contract.Name, Contract> contractNameContractMap = new EnumMap<>(Contract.Name.class);
    private Map<Script, Contract.Name> scriptContractNameMap = new HashMap<>();

    public void register(Contract.Name contractName, Contract contract) {
        Objects.requireNonNull(contractName);
        Objects.requireNonNull(contract);
        contractNameContractMap.put(contractName, contract);
        Script script = contract.createScript(new byte[0]);
        scriptContractNameMap.put(script, contractName);
    }

    @Override
    public Contract getContract(Contract.Name contractName) {
        return contractNameContractMap.get(contractName);
    }

    @Override
    public Contract getContract(Script script) {
        return getContract(getContractName(script));
    }

    @Override
    public Contract.Name getContractName(Script script) {
        if (script == null) {
            return null;
        }
        Script key = Script.builder()
                .setArgs(new byte[0])
                .setCodeHash(script.getCodeHash())
                .setHashType(script.getHashType())
                .build();
        return scriptContractNameMap.get(key);
    }
}
