package org.ckbj.crypto;

import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignTest {
    @Test
    public void testSignMessage() {
        ECKeyPair keyPair = ECKeyPair.create(Hex.toByteArray("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"));
        byte[] message = new byte[]{0x01};
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
        byte[] message = Hex.toByteArray("0xd058b58f0b2cd0612f1619f3936dd1012cbc1a1616e02d1d83d43ea0129cb650");
        byte[] signature = Hex.toByteArray("0x2c6401216c9031b9a6fb8cbfccab4fcec6c951cdf40e2320108d1856eb532250576865fbcd452bcdc4c57321b619ed7a9cfd38bd973c3e1e0243ac2777fe9d5b1b");
        ECKeyPair.Point publicKey = ECKeyPair.Point.decode(Hex.toByteArray("0x04b578ab68710d088be09833968252a1f4bde6bb36886faa3cf8e02b37006c1b0b5e56f767264c82b428292d83098071f562e288b84f7675dfea8955603959592a"));

        byte v = signature[64];
        if (v < 27) {
            v += 27;
        }

        Sign.SignatureData sd = new Sign.SignatureData(
                v,
                Arrays.copyOfRange(signature, 0, 32),
                Arrays.copyOfRange(signature, 32, 64));

        boolean match = false;
        ECKeyPair.Point recoveredPublicKey = null;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            recoveredPublicKey =
                    Sign.recoverFromSignature(
                            (byte) i,
                            new ECDSASignature(
                                    new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                            message);
            if (publicKey.equals(recoveredPublicKey)) {
                match = true;
                break;
            }

        }
        assertEquals(publicKey, recoveredPublicKey);
        assertTrue(match);
    }
}
