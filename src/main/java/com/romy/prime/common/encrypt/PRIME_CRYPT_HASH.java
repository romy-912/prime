package com.romy.prime.common.encrypt;

import com.romy.prime.common.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * packageName    : com.romy.prime.common.encrypt
 * fileName       : ICT_CRYPT_HASH
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : 비밀번호 암호화
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
public class PRIME_CRYPT_HASH {

    /**
     * make a new random salt
     */
    public static String getNewSalt() {
        /*
         * http://docs.oracle.com/javase/1.4.2/docs/api/
         * Random Number Generation (RNG) Algorithms
         * The algorithm names in this section can be specified when generating
         * an instance of SecureRandom.
         * SHA1PRNG: The name of the pseudo-random number generation
         * (PRNG) algorithm supplied by the SUN provider.
         * This implementation follows the IEEE P1363 standard,
         * Appendix G.7: "Expansion of source bits",
         * and uses SHA-1 as the foundation of the PRNG.
         * It computes the SHA-1 hash over a true-random seed value concatenated
         * with a 64-bit counter which is incremented by 1 for each operation.
         * From the 160-bit SHA-1 output, only 64 bits are used.
         */
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");  // Uses a secure Random not a simple Random
            byte[] bSalt = new byte[8]; // Salt generation 64 bits long
            random.nextBytes(bSalt);

            return byteToBase64(bSalt);
        } catch (NoSuchAlgorithmException e) {
            Log.Error(e.getMessage());
            return "";
        }
    }

    /**
     * From a password, a number of iterations and a salt, returns the
     */
    public static String getHash(String password, String strSalt, int ITERATION_NUMBER) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] salt = base64ToByte(strSalt);

        digest.update(salt);

        byte[] input = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        /* ----------------------------------------------------------------------
         *      https://www.owasp.org/index.php/Hashing_Java
         *      A minimum of 1000 operations is recommended in RSA PKCS5 standard.
         *      The stored password looks like this :
         *           Hash(hash(hash(hash(??╈?╈??.hash(password||salt)))))))))))))))
         * ---------------------------------------------------------------------- */
        for (int i = 0; i < ITERATION_NUMBER; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return byteToBase64(input);
    }

    /**
     * From a base 64 representation, returns the corresponding byte[]
     */
    public static byte[] base64ToByte(String data) {

        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(data);
    }

    /**
     * From a byte[] returns a base 64 representation
     */
    public static String byteToBase64(byte[] data) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

}
