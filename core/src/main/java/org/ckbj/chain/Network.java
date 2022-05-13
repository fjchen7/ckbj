package org.ckbj.chain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.ckbj.rpc.GsonFactory;

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

    private Map<Contract.Standard, Contract> contracts = new HashMap<>();

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
        Type type = new TypeToken<Map<Contract.Standard, Contract>>() {}.getType();

        Map<Contract.Standard, Contract> contracts = gson.fromJson(reader, type);
        for (Map.Entry<Contract.Standard, Contract> entry: contracts.entrySet()) {
            Objects.requireNonNull(entry.getKey());
            register(entry.getKey(), entry.getValue());
        }
    }

    private void register(Contract.Standard name, Contract contract) {
        contracts.put(name, contract);
    }

    public Contract get(Contract.Standard name) {
        return contracts.get(name);
    }
}
