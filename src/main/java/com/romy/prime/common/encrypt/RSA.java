package com.romy.prime.common.encrypt;

import com.romy.prime.common.Log;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

/**
 * packageName    : com.romy.prime.common.encrypt
 * fileName       : RSA
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : RSA 암호화 관련
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
@Getter
@Setter
public class RSA {

    private String publicKeyModulus = "";
    private String publicKeyExponent = "";
    private String privateKey = "";

    public static final String secretKey = "sEcreTKgIcTauTHKeysIcTTemaUth1@3";
    public static final String encryptAlgorithm = "AES/CBC/PKCS5Padding";

    public static final byte[] ivBytes = {0x00, 0x00, 0x01, 0x02, 0x00, 0x03, 0x00, 0x04, 0x05, 0x06, 0x07, 0x00, 0x00, 0x08, 0x00, 0x09};

    public static RSA getEncKey() {

        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            Log.Error(e.getMessage());
            return null;
        } // RSA키 제네레이터 생성
        generator.initialize(2048); // 키 사이즈

        KeyPair keyPair = generator.genKeyPair();

        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            Log.Error(e.getMessage());
            return null;
        }

        PublicKey publicKey = keyPair.getPublic(); // 공개키
        PrivateKey privateKey = keyPair.getPrivate(); // 개인키

        RSAPublicKeySpec publicSpec;
        try {
            publicSpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        } catch (InvalidKeySpecException e) {
            Log.Error(e.getMessage());
            return null;
        }
        String publicKeyModulus = publicSpec.getModulus().toString(16);
        String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

        String strPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        RSA rsa = new RSA();
        rsa.setPrivateKey(strPrivateKey);
        rsa.setPublicKeyExponent(publicKeyExponent);
        rsa.setPublicKeyModulus(publicKeyModulus);

        return rsa;
    }

    public static boolean dec(String privateKey, String encString) {
        if (privateKey == null) {
            throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
        }
        try {
            decryptRsa(privateKey, encString);
            return true;
        } catch (Exception e) {
            Log.Error(e.getMessage());
            return false;
        }
    }

    public static String decryptRsa(String stringPrivateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        // 문자 인코딩 주의.
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2f)] = value;
        }
        return bytes;
    }

    /**
     * 개인키 파싱
     */
    public static PrivateKey pemToPrivateKey(String privateKeyPem) throws IOException,
            NoSuchAlgorithmException, InvalidKeySpecException {
        PemReader pemReader = new PemReader(new StringReader(privateKeyPem));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();

        byte[] privateKeyBytes = pemObject.getContent();
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(privateKeySpec);
    }

    /**
     * 공개키 파싱
     */
    public static PublicKey pemToPublicKey(String publicKeyPem) throws IOException,
            NoSuchAlgorithmException, InvalidKeySpecException {
        PemReader permReader = new PemReader(new StringReader(publicKeyPem));
        PemObject pemObject = permReader.readPemObject();
        permReader.close();

        byte[] publicKeyBytes = pemObject.getContent();
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(publicKeySpec);
    }

}
