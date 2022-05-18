package org.ckbj.utils;

import org.ckbj.type.Cell;
import org.ckbj.type.Script;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CapacityTest {
    @Test
    public void testUnitConversion() {
        Assertions.assertEquals(1234500000000L, Capacity.bytesToShannon(12345));
        Assertions.assertEquals(12345, Capacity.shannonToBytes(1234500000000L));

        Assertions.assertEquals(12345000000L, Capacity.bytesToShannon(123.45));
        Assertions.assertEquals(123.45, Capacity.shannonToBytes(12345000000L));

        Assertions.assertEquals(12000000, Capacity.bytesToShannon(0.12));
        Assertions.assertEquals(0.12, Capacity.shannonToBytes(12000000));

        Assertions.assertEquals(0, Capacity.bytesToShannon(0));
        Assertions.assertEquals(0, Capacity.shannonToBytes(0));
    }

    @Test
    public void testOccupation() {
        Cell cell = Cell.builder()
                .setCapacity(100000000000L)
                .setLock(Script.builder()
                                 .setCodeHash("0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88")
                                 .setArgs("0x59a27ef3ba84f061517d13f42cf44ed020610061")
                                 .build())
                .setData("0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88")
                .build();
        Assertions.assertEquals(6100000000L, Capacity.occupation(cell, false));
        Assertions.assertEquals(9300000000L, Capacity.occupation(cell, true));
    }
}
