package org.ckbj.chain.sign;

import org.ckbj.chain.Contract;
import org.ckbj.chain.Network;
import org.ckbj.chain.NetworkDetail;
import org.ckbj.type.Script;

import java.util.HashSet;
import java.util.Set;

public abstract class StandardLockScriptSigner extends LockScriptSigner {
    protected Set<NetworkDetail> networkDetails;

    public StandardLockScriptSigner() {
        networkDetails = new HashSet<>();
        networkDetails.add(NetworkDetail.defaultInstance(Network.AGGRON));
        networkDetails.add(NetworkDetail.defaultInstance(Network.LINA));
    }

    @Override
    public boolean match(Script script) {
        if (script == null) {
            return false;
        }
        boolean contractUsed = false;
        for (NetworkDetail networkDetail : networkDetails) {
            if (networkDetail.contractUsed(script, getContractName())) {
                contractUsed = true;
            }
        }
        if (contractUsed) {
            return doMatch(script.getArgs());
        } else {
            return false;
        }
    }

    protected abstract boolean doMatch(byte[] scriptArgs);

    public abstract Contract.Standard getContractName();
}
