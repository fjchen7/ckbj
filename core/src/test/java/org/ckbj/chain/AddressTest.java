package org.ckbj.chain;

import org.ckbj.chain.address.Address;
import org.ckbj.type.Script;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.ckbj.chain.address.Address.Encoding.*;

public class AddressTest {
    private Script script = new Script()
            .setCodeHash("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
            .setArgs("0xb39bbc0b3673c7d36450bc14cfcdad2d559c6c64")
            .setHashType(Script.HashType.TYPE);

    @Test
    @SuppressWarnings("deprecation")
    public void testEncode() {
        Address address = Address.from(script, Network.LINA);
        Assertions.assertEquals("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v",
                address.encode(SHORT));
        Assertions.assertEquals("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks",
                address.encode(LONG_BECH32));
        Assertions.assertEquals("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4",
                address.encode(LONG_BECH32M));
    }

    @Test
    public void testDecode() {
        Address expected = Address.from(script, Network.LINA);
        // short format
        Address actual = Address.from("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v");
        Assertions.assertEquals(expected, actual);
        // long bech32 format
        actual = Address.from("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks");
        Assertions.assertEquals(expected, actual);
        // long bech32m format
        actual = Address.from("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4");
        Assertions.assertEquals(expected, actual);
    }
}
