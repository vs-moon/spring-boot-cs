package org.xiao.cs.sso.box.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtils {

    private static final String DEFAULT_ALGORITHM = "RSA";
    private static final int DEFAULT_KEY_SIZE = 2048;

    public static PublicKey getPublicKey(String filename) throws Exception {
        return getPublicKey(readFile(filename));
    }

    public static PublicKey getPublicKey(byte [] bytes) throws Exception {
        return KeyFactory
                .getInstance(DEFAULT_ALGORITHM)
                .generatePublic(new X509EncodedKeySpec(Base64
                        .getDecoder()
                        .decode(bytes)));
    }

    public static PrivateKey getPrivateKey(String filename) throws Exception {
        return getPrivateKey(readFile(filename));
    }

    public static PrivateKey getPrivateKey(byte [] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return KeyFactory
                .getInstance(DEFAULT_ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(Base64
                        .getDecoder()
                        .decode(bytes)));
    }

    public static KeyPair generate (String secret, int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(DEFAULT_ALGORITHM);
        keyPairGenerator.initialize(Math.max(keySize, DEFAULT_KEY_SIZE), new SecureRandom(secret.getBytes()));
        return keyPairGenerator.genKeyPair();
    }

    public static void generateAndWrite (String pubFilename, String priFilename, String secret, int keySize) throws Exception {
        KeyPair keyPair = generate(secret, keySize);
        writeFile(pubFilename, Base64.getEncoder().encode(keyPair.getPublic().getEncoded()));
        writeFile(priFilename, Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()));
    }

    public static byte [] readFile (String fileName) throws Exception {
        return Files.readAllBytes(new File(fileName).toPath());
    }

    private static void writeFile (String destinationPath, byte [] bytes) throws IOException {
        File destination = new File(destinationPath);
        if (destination.createNewFile()) {
            Files.write(destination.toPath(), bytes);
        }
    }
}
