package org.ckbj.chain.contract;

import org.ckbj.crypto.ECKeyPair;
import org.ckbj.crypto.Sign;
import org.ckbj.molecule.Serializer;
import org.ckbj.type.*;
import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class Secp256k1Blake160SighashAllTest {

    @Test
    void testCreateArgs() {
        // ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq07fzlmn6p52hsqv24gaufdqdqfcj223ug92c4sg
        ECKeyPair keyPair = ECKeyPair.create(new BigInteger("94792549824565121718037239626101186211400998112465652044858772198875205375860"));
        byte[] args = Secp256k1Blake160SighashAll.createArgs(keyPair.getPublicKey());
        Assertions.assertArrayEquals(
                Hex.toByteArray("0xfe48bfb9e83455e0062aa8ef12d03409c494a8f1"),
                args);
    }

    @Test
    void testFulfillmentFulfill() {
        Transaction tx = getTransaction();
        List<Cell> inputDetails = new ArrayList<>();
        inputDetails.add(Cell.builder()
                                 .setCapacity(60000000000000L)
                                 .setLock(Script.builder()
                                                  .setCodeHash("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
                                                  .setArgs("0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b")
                                                  .build())
                                 .build());

        ECKeyPair ecKeyPair = ECKeyPair.create("0x6fc935dad260867c749cf1ba6602d5f5ed7fb1131f1beb65be2d342e912eaafe");
        Secp256k1Blake160SighashAll.Fulfillment fulfillment = Secp256k1Blake160SighashAll.fulfillment(ecKeyPair);
        fulfillment.fulfill(tx, inputDetails);
        Assertions.assertEquals(
                "0x550000001000000055000000550000004100000090b18cc17b8c67e20075ffcffe82d079e0b6a78cb3184157d78962bdd5a648d82c9bc8e1bbe87e7b8b0661440c1060f939be85d26742148e08dc58743a900df401",
                Hex.toHexString(tx.getWitnesses().get(0)));
    }

    @Test
    public void testSignerSign() {
        Transaction tx = getTransaction();
        ECKeyPair ecKeyPair = ECKeyPair.create("0x6fc935dad260867c749cf1ba6602d5f5ed7fb1131f1beb65be2d342e912eaafe");
        byte[] witnessPlaceHolder = Serializer.serialize(WitnessArgs.builder().setLock(new byte[Sign.SIGNATURE_LENGTH]).build());
        Secp256k1Blake160SighashAll.Signer signer = new Secp256k1Blake160SighashAll.Signer(ecKeyPair);
        byte[] signature = signer.sign(tx, witnessPlaceHolder, 0);
        Assertions.assertEquals(
                "0x90b18cc17b8c67e20075ffcffe82d079e0b6a78cb3184157d78962bdd5a648d82c9bc8e1bbe87e7b8b0661440c1060f939be85d26742148e08dc58743a900df401",
                Hex.toHexString(signature));
    }

    @Test
    private Transaction getTransaction() {
        // https://pudge.explorer.nervos.org/transaction/0x7be5f1df2c5eb2f33bcf20603774e485c78ab7616e059908715b4a8200e8949f
        Transaction tx = Transaction.builder()
                .addCellDep(CellDep.DepType.DEP_GROUP, "0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", 0)
                .addCellDep(CellDep.DepType.DEP_GROUP, "0xec26b0f85ed839ece5f11c4c4e837ec359f5adc4420410f6453b1f6b60fb96a6", 0)
                .addInput("0xbaf3371f487a0d40f8ebc341a34b93a2d36e1d9f77b9533fb8c579c87958b7aa", 0)
                .addOutputs(Cell.builder()
                                    .setCapacity(0x2540be400L)
                                    .setLock(Script.builder()
                                                     .setCodeHash("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
                                                     .setArgs("0x05a1fabfa84db9e538e2e7fe3ca9adf849f55ce0")
                                                     .build())
                                    .setData(new byte[]{})
                                    .build())
                .addOutputs(Cell.builder()
                                    .setCapacity(0x368f7cadfb00L)
                                    .setLock(Script.builder()
                                                     .setCodeHash("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
                                                     .setArgs("0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b")
                                                     .build())
                                    .setData(new byte[]{})
                                    .build())
                .addWitnesses(Serializer.serialize(WitnessArgs.builder().setLock(new byte[65]).build()))
                .build();
        return tx;
    }
}