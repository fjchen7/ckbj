package io.github.fjchen7.ckbj.chain.address;

import io.github.fjchen7.ckbj.chain.Network;
import io.github.fjchen7.ckbj.type.Script;
import org.junit.jupiter.api.Test;

import static io.github.fjchen7.ckbj.chain.address.Address.Format.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        // ACP address with 21 bytes ars
        actual = Address.decode("ckb1qypylv479ewscx3ms620sv34pgeuz6zagaaqcehzz9g");
        Script script = Script.builder()
                .setCodeHash("0xd369597ff47f29fbc0d47d2e3775370d1250b85140c670e4718af712983a2354")
                .setArgs("0x4fb2be2e5d0c1a3b8694f832350a33c1685d477a0c")
                .setHashType(Script.HashType.TYPE)
                .build();
        expected = new Address(script, Network.MAINNET);
        assertEquals(expected, actual);
    }

    @Test
    public void testValidDecode() {
        // These invalid addresses come form https://github.com/nervosnetwork/ckb-sdk-rust/pull/7/files
        // INVALID bech32 encoding
        assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qyqylv479ewscx3ms620sv34pgeuz6zagaaqh0knz7"));
        // INVALID data length
        assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qyqylv479ewscx3ms620sv34pgeuz6zagaarxdzvx03"));
        // INVALID code hash index
        assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qyg5lv479ewscx3ms620sv34pgeuz6zagaaqajch0c"));
        // INVALID bech32m encoding
        assertThrows(AddressFormatException.class, () -> Address.decode("ckb1q2da0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsnajhch96rq68wrqn2tmhm"));
        // Invalid ckb2021 format full address
        assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq20k2lzuhgvrgacv4tmr88"));
        assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqz0k2lzuhgvrgacvhcym08"));
        assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqj0k2lzuhgvrgacvnhnzl8"));
    }
}

