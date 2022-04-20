package org.ckbj.chain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.ckbj.rpc.GsonFactory;
import org.ckbj.type.Script;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class NetworkDetail {
    private Network network;
    private Map<String, Contract> contracts = new HashMap<>();

    private static NetworkDetail LINA_NETWORK_DETAIL;
    private static NetworkDetail AGGRON_NETWORK_DETAIL;

    static {
        try {
            LINA_NETWORK_DETAIL = new NetworkDetail(Network.LINA, "/lina.json");
            AGGRON_NETWORK_DETAIL = new NetworkDetail(Network.AGGRON, "/aggron.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public NetworkDetail(Network network) {
        this.network = network;
    }

    public NetworkDetail(Network network, String configurationPath) throws IOException {
        this(network);
        List<Contract> contractList = loadContracts(configurationPath);
        for (Contract contract: contractList) {
            register(contract);
        }
    }

    public static NetworkDetail defaultInstance(Network network) {
        Objects.requireNonNull(network);
        switch (network) {
            case LINA:
                return LINA_NETWORK_DETAIL;
            case AGGRON:
                return AGGRON_NETWORK_DETAIL;
            default:
                throw new IllegalArgumentException("Unknown network");
        }
    }

    public Network getNetwork() {
        return network;
    }

    public void register(Contract contract) {
        contracts.put(contract.getName(), contract);
    }

    public Contract get(String contract) {
        return contracts.get(contract);
    }

    public Contract get(Contract.Standard contract) {
        return get(contract.name());
    }

    /**
     * Check if the script uses binary code of given contract.
     *
     * @return true if the script uses binary code of given contract.
     */
    public boolean contractUsed(Script script, Contract.Standard contract) {
        return contractUsed(script, contract.name());
    }

    /**
     * Check if the script uses code of given contract.
     *
     * @return true if the script uses code of given contract.
     */
    public boolean contractUsed(Script script, String contractName) {
        return contractUsed(script, get(contractName));
    }

    public boolean contractUsed(Script script, Contract contract) {
        if (contract == null) {
            return false;
        }
        return Arrays.equals(script.getCodeHash(), contract.getCodeHash());
    }

    private static List<Contract> loadContracts(String path) throws IOException {
        Class clazz = NetworkDetail.class;
        Reader reader = Files.newBufferedReader(
                Paths.get(clazz.getResource(path).getPath()));
        Gson gson = GsonFactory.create();
        Type type = new TypeToken<List<Contract>>() {}.getType();
        return gson.fromJson(reader, type);
    }
}
