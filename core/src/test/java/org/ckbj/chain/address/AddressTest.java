package org.ckbj.chain.address;

import org.ckbj.chain.Network;
import org.ckbj.type.Script;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.ckbj.chain.address.Address.Format.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressTest {
    private Script script = Script.builder()
            .setCodeHash("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
            .setArgs("0xb39bbc0b3673c7d36450bc14cfcdad2d559c6c64")
            .setHashType(Script.HashType.TYPE)
            .build();

    @Test
    @SuppressWarnings("deprecation")
    public void testEncodedFormat() {
        assertEquals(SHORT,
                     Address.encodedFormat("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v"));
        assertEquals(FULL_BECH32,
                     Address.encodedFormat("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks"));
        assertEquals(FULL_BECH32M,
                     Address.encodedFormat("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testEncode() {
        Address address = new Address(script, Network.MAINNET);
        assertEquals("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v",
                     address.encode(SHORT));
        assertEquals("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks",
                     address.encode(FULL_BECH32));
        assertEquals("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4",
                     address.encode(FULL_BECH32M));
    }

    @Test
    public void testDecode() {
        Address expected = new Address(script, Network.MAINNET);
        // short format
        Address actual = Address.decode("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v");
        assertEquals(expected, actual);
        // long bech32 format
        actual = Address.decode("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks");
        assertEquals(expected, actual);
        // long bech32m format
        actual = Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4");
        assertEquals(expected, actual);
    }

    @Test
    public void testValidDecode() {
        // How to get the error address
        // original address: ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqgvf0k9sc40s3azmpfvhyuudhahpsj72tsr8cx3d
        // change payload[0] to be 0x00 in Address#encodeFullBech32m, and then decode(FULL_BECH32M);
        Assertions.assertThrows(AddressFormatException.class, () -> {
            Address.decode("ckb1q2da0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqgvf0k9sc40s3azmpfvhyuudhahpsj72tsqsf9mm");
        });
    }
}

