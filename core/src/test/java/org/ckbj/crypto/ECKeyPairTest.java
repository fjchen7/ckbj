package org.ckbj.crypto;

import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

class ECKeyPairTest {
    @Test
    public void testConstructor() {
        ECKeyPair keyPair = ECKeyPair.create(Hex.toByteArray("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"));

        BigInteger x = new BigInteger("4a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01", 16);
        BigInteger y = new BigInteger("d1868c7dabbf50e52ca7311e1263f917a8ced1d033e82dc2a68bed69397382f4", 16);
        ECKeyPair.Point point = new ECKeyPair.Point(x, y);
        Assertions.assertEquals(point, keyPair.getPublicKey());
    }

    @Test
    public void testSign() {
        ECKeyPair keyPair = ECKeyPair.create(Hex.toByteArray("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"));
        byte[] message = new byte[]{0x01};

        ECDSASignature signature = keyPair.sign(message);
        Assertions.assertEquals(
                new BigInteger("99970301002729623952716936889563242735270150204421303783162626130186767091035"),
                signature.r);
        Assertions.assertEquals(
                new BigInteger("26111814270953384674339719806755587351330051979495799375085430243554275015926"),
                signature.s);
    }

    @Test
    public void testPoint() {
        BigInteger x = new BigInteger("4a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01", 16);
        BigInteger y = new BigInteger("d1868c7dabbf50e52ca7311e1263f917a8ced1d033e82dc2a68bed69397382f4", 16);
        ECKeyPair.Point point = new ECKeyPair.Point(x, y);

        byte[] compressedEncoded = Hex.toByteArray("024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
        byte[] uncompressedEncoded = Hex.toByteArray("044a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01d1868c7dabbf50e52ca7311e1263f917a8ced1d033e82dc2a68bed69397382f4");
        Assertions.assertArrayEquals(compressedEncoded, point.encode(true));
        Assertions.assertArrayEquals(uncompressedEncoded, point.encode(false));

        Assertions.assertEquals(point, ECKeyPair.Point.decode(compressedEncoded));
        Assertions.assertEquals(point, ECKeyPair.Point.decode(uncompressedEncoded));
    }

    @Test
    public void testRandom() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair keyPair = ECKeyPair.random();
        Assertions.assertNotNull(keyPair);
    }
}