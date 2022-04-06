package org.ckbj.crypto;

import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ECKeyPairTest {
    private ECKeyPair keyPair = ECKeyPair.create(Hex.toByteArray("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"));
    private byte[] message = new byte[]{0x01};

    @Test
    public void testConstructor() {

    }

    @Test
    public void testSign() {
        ECDSASignature signature = keyPair.sign(message);
        Assertions.assertEquals(
                new BigInteger("99970301002729623952716936889563242735270150204421303783162626130186767091035"),
                signature.r);
        Assertions.assertEquals(
                new BigInteger("26111814270953384674339719806755587351330051979495799375085430243554275015926"),
                signature.s);
    }

    @Test
    public void test() {
        Sign.SignatureData signatureData = Sign.signMessage(message, keyPair, false);
        Assertions.assertArrayEquals(
                Hex.toByteArray("0xdd052f69a76afa6c38d06be785053f2ad8d8e900e8db9eee81ac784c550ec55b"),
                signatureData.getR());
        Assertions.assertArrayEquals(
                Hex.toByteArray("0x39bac35cfe890e4007cea21d82cbcb446b20b2d8d79d08449d71981a330abcf6"),
                signatureData.getS());
    }

    @Test
    public void testRecoverFromSignature() {
        String signature =
                "0x2c6401216c9031b9a6fb8cbfccab4fcec6c951cdf40e2320108d1856eb532250576865fbcd452bcdc4c57321b619ed7a9cfd38bd973c3e1e0243ac2777fe9d5b1b";
        byte[] message = Hex.toByteArray("0xd058b58f0b2cd0612f1619f3936dd1012cbc1a1616e02d1d83d43ea0129cb650");
        BigInteger publicKey = new BigInteger("9504426613731415939928550371086801615293343087496148015624297578185901893741584144413126517981259517547849760054550238081275639855491317161874566034315562");

        byte[] signatureBytes = Hex.toByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        Sign.SignatureData sd = new Sign.SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        boolean match = false;
        BigInteger recoveredPublicKey = null;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            recoveredPublicKey =
                    Sign.recoverFromSignature(
                            (byte) i,
                            new ECDSASignature(
                                    new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                            message);
            if (publicKey != null && publicKey.compareTo(recoveredPublicKey) == 0) {
                match = true;
                break;
            }

        }

        assertEquals(publicKey, recoveredPublicKey);
        assertTrue(match);
    }
}