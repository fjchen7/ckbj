package org.ckbj.chain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.ckbj.chain.address.Address;
import org.ckbj.rpc.GsonFactory;
import org.ckbj.type.Script;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Network {
    MAINNET,
    TESTNET;

    private Map<Contract.Type, Contract> contractTypeContractMap = new HashMap<>();
    private Map<Script, Contract.Type> scriptContractTypeMap = new HashMap<>();

    static {
        try {
            MAINNET.loadContracts("/mainnet.json");
            TESTNET.loadContracts("/testnet.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadContracts(String path) throws IOException {
        Reader reader = Files.newBufferedReader(
                Paths.get(Network.class.getResource(path).getPath()));
        Gson gson = GsonFactory.create();
        Type type = new TypeToken<Map<Contract.Type, Contract>>() {}.getType();

        Map<Contract.Type, Contract> contracts = gson.fromJson(reader, type);
        for (Map.Entry<Contract.Type, Contract> entry: contracts.entrySet()) {
            Objects.requireNonNull(entry.getKey());
            register(entry.getKey(), entry.getValue());
        }
    }

    private void register(Contract.Type contractType, Contract contract) {
        Objects.requireNonNull(contractType);
        Objects.requireNonNull(contract);
        contractTypeContractMap.put(contractType, contract);
        Script script = contract.createScript(new byte[0]);
        scriptContractTypeMap.put(script, contractType);
    }

    public Contract getContract(Contract.Type contractType) {
        return contractTypeContractMap.get(contractType);
    }

    public Contract getContract(Script script) {
        return getContract(getContractType(script));
    }

    /**
     * get contract type used by script
     *
     * @param script
     * @return return contract type used by script, or null if not found
     */
    public Contract.Type getContractType(Script script) {
        Script key = Script.builder()
                .setArgs(new byte[0])
                .setCodeHash(script.getCodeHash())
                .setHashType(script.getHashType())
                .build();
        return scriptContractTypeMap.get(key);
    }

    public Address createAddress(Contract.Type contractType, byte[] args) {
        Script script = contractTypeContractMap.get(contractType).createScript(args);
        return new Address(script, this);
    }
}
