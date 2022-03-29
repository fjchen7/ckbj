package org.ckbj;

import org.ckbj.protocol.type.CellDep;
import org.ckbj.protocol.type.Script;

import java.util.HashSet;
import java.util.Set;

public class Config {
    private Set<ScriptInfo> scriptInfoSet;

    public static Config TESTNET_CONFIG = new Config()
            .register(
                    // secp256k1_blake160
                    new ScriptInfo.ScriptInfoBuilder()
                            .addCellDep("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", 0, CellDep.DepType.DEP_GROUP)
                            .setCodeHash("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
                            .setHashType(Script.HashType.TYPE)
                            .build())
            .register(
                    // sudt
                    new ScriptInfo.ScriptInfoBuilder()
                            .addCellDep("0xe12877ebd2c3c364dc46c5c992bcfaf4fee33fa13eebdf82c591fc9825aab769", 0, CellDep.DepType.CODE)
                            .setCodeHash("0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63")
                            .setHashType(Script.HashType.TYPE)
                            .build()
            );

    public static Config MAINNET_CONFIG = new Config();

    public Config() {
        scriptInfoSet = new HashSet<>();
    }

    public Config register(ScriptInfo scriptInfo) {
        scriptInfoSet.add(scriptInfo);
        return this;
    }

    public ScriptInfo getScriptInfo(Script script) {
        for (ScriptInfo scriptInfo: scriptInfoSet) {
            if ( scriptInfo.codeHash.equals(script.getCodeHash()) && scriptInfo.hashType == script.getHashType() ) {
                return scriptInfo;
            }
        }
        return null;
    }

}
