package org.ckbj.chain.contract;

import org.ckbj.crypto.ECKeyPair;
import org.ckbj.type.Cell;
import org.ckbj.type.CellDep;
import org.ckbj.type.Script;
import org.ckbj.type.Transaction;
import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Secp256k1Blake160MultisigAllTest {
    @Test
    public void testEncodeDecode() {
        byte[] encoded = Hex.toByteArray("0x000002029b41c025515b00c24e2e2042df7b221af5c1891fe732dcd15b7618eb1d7a11e6a68e4579b5be0114");

        Secp256k1Blake160MultisigAll.Rule rule = Secp256k1Blake160MultisigAll.Rule.builder()
                .addKey(Hex.toByteArray("0x9b41c025515b00c24e2e2042df7b221af5c1891f"))
                .addKey(Hex.toByteArray("0xe732dcd15b7618eb1d7a11e6a68e4579b5be0114"))
                .setThreshold(2)
                .build();
        Assertions.assertArrayEquals(encoded, rule.encode());
        Assertions.assertEquals(rule, Secp256k1Blake160MultisigAll.Rule.decode(encoded));
    }

    @Test
    void testCreateArgs() {
        /**
         * https://pudge.explorer.nervos.org/address/ckt1qyqntmtmjwd54jwtg3acydq0mrex6dz00f3qsq0qt4
         * The generated 2-2 multisig address:
         * - ckt1qyqntmtmjwd54jwtg3acydq0mrex6dz00f3qsq0qt4
         * - 0x35ed7b939b4ac9cb447b82340fd8f26d344f7a62
         * #0
         * - ckt1qyqfkswqy4g4kqxzfchzqskl0v3p4awp3y0s2gxul9
         * - 0x9b41c025515b00c24e2e2042df7b221af5c1891f
         * - 5271b0e474609ee280eb6ba07895718863a0eb8f114afd7217fa371fd48f6941
         * #1
         * - ckt1qyqwwvku69dhvx8tr4apre4x3ezhndd7qy2q7cke79
         * - 0xe732dcd15b7618eb1d7a11e6a68e4579b5be0114
         * - bb3597c4daf5e2435fd47aeeb32847df32f1c710a6475f639e34b2275607eaa3
         */
        Secp256k1Blake160MultisigAll.Rule rule = Secp256k1Blake160MultisigAll.Rule.builder()
                .addKey(Hex.toByteArray("0x9b41c025515b00c24e2e2042df7b221af5c1891f"))
                .addKey(Hex.toByteArray("0xe732dcd15b7618eb1d7a11e6a68e4579b5be0114"))
                .setThreshold(2)
                .build();

        byte[] args = Secp256k1Blake160MultisigAll.createArgs(rule);
        Assertions.assertArrayEquals(
                args,
                Hex.toByteArray("0x35ed7b939b4ac9cb447b82340fd8f26d344f7a62"));
    }

    @Test
    public void testSignerSign() {
        Transaction tx = getTransaction();
        Secp256k1Blake160MultisigAll.Rule rule = Secp256k1Blake160MultisigAll.Rule.builder()
                .addKey(Hex.toByteArray("0x9b41c025515b00c24e2e2042df7b221af5c1891f"))
                .addKey(Hex.toByteArray("0xe732dcd15b7618eb1d7a11e6a68e4579b5be0114"))
                .setThreshold(2)
                .build();
        ECKeyPair keyPair = ECKeyPair.create("bb3597c4daf5e2435fd47aeeb32847df32f1c710a6475f639e34b2275607eaa3");
        Secp256k1Blake160MultisigAll.Signer signer = new Secp256k1Blake160MultisigAll.Signer(rule, keyPair);
        byte[] signature = signer.sign(tx, 0);
        Assertions.assertEquals(
                "0x309f6a35043ee852c77d525ab8181115467db6161ab2f2916ce53af28d855c5d6d3ca6efeeb640cf2d5e54f6173dfe5e8b69e163e8a34b952cb1eb233247a10001",
                Hex.toHexString(signature));
    }

    @Test
    public void testFulfillment() {
        Transaction tx = getTransaction();
        Secp256k1Blake160MultisigAll.Rule rule = Secp256k1Blake160MultisigAll.Rule.builder()
                .addKey(Hex.toByteArray("0x9b41c025515b00c24e2e2042df7b221af5c1891f"))
                .addKey(Hex.toByteArray("0xe732dcd15b7618eb1d7a11e6a68e4579b5be0114"))
                .setThreshold(2)
                .build();

        Secp256k1Blake160MultisigAll.Fulfillment fulfillment = new Secp256k1Blake160MultisigAll.Fulfillment(
                rule,
                Hex.toByteArray("0x309f6a35043ee852c77d525ab8181115467db6161ab2f2916ce53af28d855c5d6d3ca6efeeb640cf2d5e54f6173dfe5e8b69e163e8a34b952cb1eb233247a10001"),
                Hex.toByteArray("0xbf990766e3efa8253c58330f4366bef09f49cbe4efa47b2b491541ad919c90c33ca6763c5780693efd0efea89c1645e8992520e8e551c1dec50fc41fac14b3a401"));
        fulfillment.fulfill(tx, 0);
        Assertions.assertEquals(
                "0xc200000010000000c2000000c2000000ae000000000002029b41c025515b00c24e2e2042df7b221af5c1891fe732dcd15b7618eb1d7a11e6a68e4579b5be0114309f6a35043ee852c77d525ab8181115467db6161ab2f2916ce53af28d855c5d6d3ca6efeeb640cf2d5e54f6173dfe5e8b69e163e8a34b952cb1eb233247a10001bf990766e3efa8253c58330f4366bef09f49cbe4efa47b2b491541ad919c90c33ca6763c5780693efd0efea89c1645e8992520e8e551c1dec50fc41fac14b3a401",
                Hex.toHexString(tx.getWitness(0)));
    }

    private Transaction getTransaction() {
        // https://pudge.explorer.nervos.org/transaction/0x8b9027c407ee95f043b158b4bb5fe685b2e6159723b48712d91ec733b3068a5c
        /**
         * Input address: The generated 2-2 multisig address:
         * - ckt1qyqntmtmjwd54jwtg3acydq0mrex6dz00f3qsq0qt4
         * - 0x35ed7b939b4ac9cb447b82340fd8f26d344f7a62
         * #0
         * - ckt1qyqfkswqy4g4kqxzfchzqskl0v3p4awp3y0s2gxul9
         * - 0x9b41c025515b00c24e2e2042df7b221af5c1891f
         * - 5271b0e474609ee280eb6ba07895718863a0eb8f114afd7217fa371fd48f6941
         * #1
         * - ckt1qyqwwvku69dhvx8tr4apre4x3ezhndd7qy2q7cke79
         * - 0xe732dcd15b7618eb1d7a11e6a68e4579b5be0114
         * - bb3597c4daf5e2435fd47aeeb32847df32f1c710a6475f639e34b2275607eaa3
         *
         * Witness #0
         * - WitnessArgs header
         *   - c200000010000000c2000000c2000000ae000000
         * - multisig script
         *   - 00000202
         *   - 9b41c025515b00c24e2e2042df7b221af5c1891f
         *   - e732dcd15b7618eb1d7a11e6a68e4579b5be0114
         * - signature #0
         *   - 309f6a35043ee852c77d525ab8181115467db6161ab2f2916ce53af28d855c5d6d3ca6efeeb640cf2d5e54f6173dfe5e8b69e163e8a34b952cb1eb233247a10001
         * - signature #1
         *   - bf990766e3efa8253c58330f4366bef09f49cbe4efa47b2b491541ad919c90c33ca6763c5780693efd0efea89c1645e8992520e8e551c1dec50fc41fac14b3a401
         */
        Transaction tx = Transaction.builder()
                .addCellDep(CellDep.DepType.DEP_GROUP, "0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", 1)
                .addInput("0xb8e52009fb4dc0d63dd2a0547909bb1d66dff83e14645c70b25222c1e04ec593", 0)
                .addOutput(Cell.builder()
                                   .setCapacity(0x2540be400L)
                                   .setLock(Script.builder()
                                                    .setCodeHash("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
                                                    .setArgs("0x6cfd0e42b63a6fccf5eda9cef74d5fd0537fd55a")
                                                    .build())
                                   .setData(new byte[]{})
                                   .build())
                .addOutput(Cell.builder()
                                   .setCapacity(0xe67aa34b00L)
                                   .setLock(Script.builder()
                                                    .setCodeHash("0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8")
                                                    .setArgs("0x35ed7b939b4ac9cb447b82340fd8f26d344f7a62")
                                                    .build())
                                   .setData(new byte[]{})
                                   .build())
                .addWitness(Hex.toByteArray("0xc200000010000000c2000000c2000000ae000000000002029b41c025515b00c24e2e2042df7b221af5c1891fe732dcd15b7618eb1d7a11e6a68e4579b5be0114309f6a35043ee852c77d525ab8181115467db6161ab2f2916ce53af28d855c5d6d3ca6efeeb640cf2d5e54f6173dfe5e8b69e163e8a34b952cb1eb233247a10001bf990766e3efa8253c58330f4366bef09f49cbe4efa47b2b491541ad919c90c33ca6763c5780693efd0efea89c1645e8992520e8e551c1dec50fc41fac14b3a401"))
                .addWitness(Hex.toByteArray("0x1234"))
                .build();
        return tx;
    }

}