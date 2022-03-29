package org.ckbj.protocol.type;

/**
 * Generalization of lock script and type script in a cell.<br>
 *
 * CKB VM runs all {@link CellInput input cells}' lock scripts, and {@link CellInput input cells} and
 * {@link CellOutput output cells}' type scripts when a new {@link Transaction} is committed.
 *
 * @see <a href="https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0022-transaction-structure/0022-transaction-structure.md">RFC22 CKB Transaction Structure</a>
 */

public class Script {
    private byte[] codeHash;
    private HashType hashType;
    private byte[] args;

    public byte[] getCodeHash() {
        return codeHash;
    }

    public HashType getHashType() {
        return hashType;
    }

    public byte[] getArgs() {
        return args;
    }

    public enum HashType {
        DATA(0x00),
        TYPE(0x01),
        DATA1(0x02);

        private byte value;

        HashType(int value) {
            this.value = (byte)value;
        }
    }

    public String toAddress() {
        return null;
    }

    public static Script fromAddress(String address) {
        return null;
    }

}
