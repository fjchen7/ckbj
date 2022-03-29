package org.ckbj.protocol.type;

import java.math.BigInteger;

/**
 * Live cell generated in {@link Transaction}.
 */
public class CellOutput {
    private BigInteger capacity;
    private Script typeScript;
    private Script lockScript;

    public BigInteger getCapacity() {
        return capacity;
    }

    public Script getTypeScript() {
        return typeScript;
    }

    public Script getLockScript() {
        return lockScript;
    }

    public CellOutput(BigInteger capacity, Script typeScript, Script lockScript) {
        this.capacity = capacity;
        this.typeScript = typeScript;
        this.lockScript = lockScript;
    }
}
