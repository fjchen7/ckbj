package org.ckbj.type;

import org.ckbj.utils.Hex;

public final class ScriptBuilder {
    private byte[] codeHash;
    private byte[] args = new byte[0];
    private Script.HashType hashType = Script.HashType.TYPE;

    ScriptBuilder() {
    }

    public ScriptBuilder setCodeHash(byte[] codeHash) {
        this.codeHash = codeHash;
        return this;
    }

    public ScriptBuilder setCodeHash(String codeHash) {
        return setCodeHash(Hex.toByteArray(codeHash));
    }

    public ScriptBuilder setArgs(byte[] args) {
        this.args = args;
        return this;
    }

    public ScriptBuilder setArgs(String args) {
        return setArgs(Hex.toByteArray(args));
    }

    public ScriptBuilder setHashType(Script.HashType hashType) {
        this.hashType = hashType;
        return this;
    }

    public Script build() {
        Script script = new Script();
        script.setCodeHash(codeHash);
        script.setArgs(args);
        script.setHashType(hashType);
        return script;
    }
}
