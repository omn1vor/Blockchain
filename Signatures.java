package blockchain;

import java.security.*;

public class Signatures {

    private static final int KEY_LEN = 1024;

    public static KeyPair generateKeys() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(KEY_LEN);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't generate keys for signing", e);
        }
    }

    public static byte[] sign(String data, PrivateKey key) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(key);
            rsa.update(data.getBytes());
            return rsa.sign();
        } catch (Exception e) {
            throw new RuntimeException("Could not sign the message", e);
        }
    }

    public static boolean verifySignature(String data, byte[] signature, PublicKey key) {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(key);
            sig.update(data.getBytes());
            return sig.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException("Could not verify the signature", e);
        }
    }
}
