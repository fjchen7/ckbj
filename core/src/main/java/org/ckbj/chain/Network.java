package org.ckbj.chain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.ckbj.chain.address.Address;
import org.ckbj.rpc.GsonFactory;
import org.ckbj.type.Script;

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
        Type type = new TypeToken<Map<Contract.Type, Contract>>() {}.getType();

        Map<Contract.Type, Contract> contracts = gson.fromJson(reader, type);
        for (Map.Entry<Contract.Type, Contract> entry: contracts.entrySet()) {
            Objects.requireNonNull(entry.getKey());
            register(entry.getKey(), entry.getValue());
        }
    }

    public void register(Contract.Type contractType, Contract contract) {
        contractCollection.register(contractType, contract);
    }

    @Override
    public Contract getContract(Contract.Type contractType) {
        return contractCollection.getContract(contractType);
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
    public Contract.Type getContractType(Script script) {
        return contractCollection.getContractType(script);
    }

    public Address createAddress(Contract.Type contractType, byte[] args) {
        Script script = getContract(contractType).createScript(args);
        return new Address(script, this);
    }
}
