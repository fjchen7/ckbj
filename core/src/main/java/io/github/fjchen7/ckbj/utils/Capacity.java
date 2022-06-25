package io.github.fjchen7.ckbj.utils;

import io.github.fjchen7.ckbj.type.Cell;
import io.github.fjchen7.ckbj.type.Script;

/**
 * See: https://github.com/nervosnetwork/ckb/wiki/Occupied-Capacity
 */
public class Capacity {
    public final static long INITIAL_SUPPLY = 336000000000L;
    public final static long BURN_QUANTITY = 84000000000L;

    /**
     * Compute occupied capacity of cell with or without data
     *
     * @return capacity in shannon
     */
    public static long occupation(Cell cell, boolean includeData) {
        long occupation = bytesToShannon(8)
                + occupation(cell.getLock())
                + occupation(cell.getType());
        if (includeData) {
            occupation += occupation(cell.getData());
        }
        return occupation;
    }

    /**
     * Compute occupied capacity of script
     *
     * @return capacity in shannon
     */
    public static long occupation(Script script) {
        if (script == null) {
            return 0;
        }
        return bytesToShannon(1)
                + occupation(script.getArgs())
                + occupation(script.getCodeHash());
    }

    /**
     * Compute occupied capacity of byte array
     *
     * @param data Byte array
     * @return capacity in shannon
     */
    private static long occupation(byte[] data) {
        if (data == null) {
            return 0;
        }
        return bytesToShannon(data.length);
    }

    /**
     * Unit conversion of capacity from bytes to shannon
     *
     * @param value capacity in bytes
     * @return capacity in shannon
     */
    public static long bytesToShannon(double value) {
        return (long) (value * 100000000L);
    }

    /**
     * Unit conversion of capacity from shannon to bytes
     *
     * @param value capacity in shannon
     * @return capacity in bytes
     */
    public static double shannonToBytes(long value) {
        return value / 100000000.0;
    }
}
