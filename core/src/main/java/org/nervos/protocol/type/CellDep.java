package org.nervos.protocol.type;


/**
 * Live cell that {@link Transaction} is dependent on.
 */
public class CellDep {
    OutPoint outPoint;
    DepType depType;

    public enum DepType {
        CODE(0x00),
        DEP_GROUP(0x01);

        private byte value;

        DepType(int value) {
            this.value = (byte) value;
        }
    }
}
