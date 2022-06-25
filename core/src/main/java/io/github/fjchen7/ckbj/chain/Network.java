package io.github.fjchen7.ckbj.chain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.fjchen7.ckbj.rpc.GsonFactory;
import io.github.fjchen7.ckbj.type.Script;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

public enum Network implements ContractCollection {
    MAINNET,
    TESTNET;

    private DefaultContractCollection contractCollection = new DefaultContractCollection();

    static {
        try {
            MAINNET.loadContracts("mainnet.json");
            TESTNET.loadContracts("testnet.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadContracts(String path) throws IOException {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
        Reader reader = new InputStreamReader(inputStream);

        Gson gson = GsonFactory.create();
        Type type = new TypeToken<Map<Contract.Name, Contract>>() {}.getType();

        Map<Contract.Name, Contract> contracts = gson.fromJson(reader, type);
        for (Map.Entry<Contract.Name, Contract> entry: contracts.entrySet()) {
            Objects.requireNonNull(entry.getKey());
            register(entry.getKey(), entry.getValue());
        }
    }

    public void register(Contract.Name contractName, Contract contract) {
        contractCollection.register(contractName, contract);
    }

    @Override
    public Contract getContract(Contract.Name contractName) {
        return contractCollection.getContract(contractName);
    }

    @Override
    public Contract getContract(Script script) {
        return contractCollection.getContract(script);
    }

    /**
     * get contract type used by script
     *
     * @param script
     * @return return contract type used by script, or null if not found
     */
    public Contract.Name getContractName(Script script) {
        return contractCollection.getContractName(script);
    }
}
