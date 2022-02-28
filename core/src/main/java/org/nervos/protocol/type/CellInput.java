package org.nervos.protocol.type;

/**
 * Live cell to be consumed in {@link Transaction}.
 */
public class CellInput {
    private OutPoint previousOutpoint;

    /**
     * 8-byte length field to prevent {@link Transaction} to be mined before an absolute or relative time.
     * @see <a href="https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0017-tx-valid-since/0017-tx-valid-since.md">RFC17 Transaction valid since</a>
     */
    private byte[] since;
}
