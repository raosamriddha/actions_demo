package org.lattice.spectrum_backend_final.dao.util;

/**
 * @author RAHUL KUMAR MAURYA
 */

import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;


/**
 * This class is used to secure the password with the help of encryption and decryption.
 */
public class PasswordUtil {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    /**
     * @param myKey
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static void setKey(String myKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance(ApiConstant.SHA_1);
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ApiConstant.AES);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new NoSuchAlgorithmException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param strToEncrypt
     * @param secret
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     */
    public static String encrypt(String strToEncrypt, String secret) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(ApiConstant.AES_ECB_PKCS5PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (UnsupportedEncodingException e) {

            System.out.println("-----Exception------" + e.getMessage());

            throw new UnsupportedEncodingException(e.getMessage());

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            throw new NoSuchAlgorithmException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        } catch (NoSuchPaddingException e) {

            e.printStackTrace();
            throw new NoSuchPaddingException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        } catch (BadPaddingException e) {

            e.printStackTrace();
            throw new BadPaddingException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        } catch (IllegalBlockSizeException e) {

            e.printStackTrace();
            throw new IllegalBlockSizeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        } catch (InvalidKeyException e) {

            e.printStackTrace();
            throw new InvalidKeyException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        }

    }

    /**
     * @param strToDecrypt
     * @param secret
     * @return
     * @throws Exception
     */
    public static String decrypt(String strToDecrypt, String secret) throws Exception {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(ApiConstant.AES_ECB_PKCS5PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (UnsupportedEncodingException e) {

            System.out.println("-----Exception------" + e.getMessage());

            throw new UnsupportedEncodingException(e.getMessage());
        } catch (Exception ex) {
        	ex.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
    }


    /**
     * Generate a random password based on security rules
     * <p>
     * - at least 8 characters, max of 12
     * - at least one uppercase
     * - at least one lowercase
     * - at least one number
     * - at least one symbol
     *
     * @return Random password based on security rules.
     */
    public static String generatePassword() {
        /**
         * Minimum length for a decent password
         */
        final int MAX_LENGTH = 8;

        /**
         * The random number generator.
         */
        java.util.Random r = new java.util.Random();

        /**
         * I, L and O are good to leave out as are numeric zero and one.
         */
        final String DIGITS = "23456789";
        final String LOCASE_CHARACTERS = "abcdefghjkmnpqrstuvwxyz";
        final String UPCASE_CHARACTERS = "ABCDEFGHJKMNPQRSTUVWXYZ";
        final String SYMBOLS = "@#$%";
        final String ALL = DIGITS + LOCASE_CHARACTERS + UPCASE_CHARACTERS + SYMBOLS;
        final char[] upcaseArray = UPCASE_CHARACTERS.toCharArray();
        char[] locaseArray = LOCASE_CHARACTERS.toCharArray();
        final char[] digitsArray = DIGITS.toCharArray();
        final char[] symbolsArray = SYMBOLS.toCharArray();
        final char[] allArray = ALL.toCharArray();


        StringBuilder sb = new StringBuilder();

        // get at least one lowercase letter
        sb.append(locaseArray[r.nextInt(locaseArray.length)]);

        // get at least one uppercase letter
        sb.append(upcaseArray[r.nextInt(upcaseArray.length)]);

        // get at least one digit
        sb.append(digitsArray[r.nextInt(digitsArray.length)]);

        // get at least one symbol
        sb.append(symbolsArray[r.nextInt(symbolsArray.length)]);

        // fill in remaining with random letters
        for (int i = 0; i < MAX_LENGTH - 4; i++) {
            sb.append(allArray[r.nextInt(allArray.length)]);
        }

        return sb.toString();
    }


}
