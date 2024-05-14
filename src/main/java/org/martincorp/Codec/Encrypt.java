package org.martincorp.Codec;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.martincorp.Interface.GUI;

public class Encrypt {
    //Variables:
    private MessageDigest hasher;
    private SecretKeyFactory skf;
    private int key_length;
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;


    //Builder:
      //So just in case anyone wants to secure change how this works/encrypts, I've left this ready to use any of the basic options provided by the standard Java libraries:
      // -1 - SHA-1, really, don't use this unless it doesn't matter if someones reverses this encryption.
      // 0 - MD5, the classic and unmatched MD5.
      // 1 - SHA-256, most used and perfectly fine.
      // 2 - SHA-512, for those who want a bit more.
      // 3 - SHA3-256, the newest and best version of hashing.
      // 4 - SHA3-512, why just stop at 2^9?
    public Encrypt(int mode){
        try{
            switch(mode){
                case -1:
                    //Why are you doing this to yourself?
                    hasher = MessageDigest.getInstance("SHA-1");
                    skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                    key_length = 160;
                    break;
                case 0:
                    hasher = MessageDigest.getInstance("MD5");
                    skf = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES");
                    key_length = 128;
                    break;
                case 1:
                    hasher = MessageDigest.getInstance("SHA-224");
                    skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA224");
                    key_length = 224;
                    break;
                case 2:
                    hasher = MessageDigest.getInstance("SHA-256");
                    skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                    key_length = 256;
                    break;
                case 3:
                    hasher = MessageDigest.getInstance("SHA-384");
                    skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA384");
                    key_length = 384;
                    break;
                case 4:
                    hasher = MessageDigest.getInstance("SHA-512");
                    skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
                    key_length = 512;
                    break;
            }
        }
        catch(NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
            GUI.launchMessage(2, "Error de encriptación", "Ha ocurrido un problema al iniciar el módulo de protección de contraseñas.");
        }
    }

    //Methods:
    public String hash(String input){
        String output = null;
        byte[] buffer = hasher.digest(input.getBytes());

        output = hex(buffer);
        return output;
    }

    public String hex(byte[] encoded){
        String decoded = null;

        BigInteger decoder = new BigInteger(1, encoded);
        decoded = decoder.toString(16);
        while(decoded.length() < 32){
            decoded = "0".concat(decoded);
        }

        return decoded;
    }

    /* Hashing & Salting (provided by 'assylias' from stackoverflow.com, check project documentation for links and info).*/
    /**
     * Returns a random salt to be used to hash a password.
     * @return a 16 bytes random salt
     */
    public byte[] getSalt(){
        byte[] salt = new byte[32];
        RANDOM.nextBytes(salt);
        return salt;
    }

    /**
     * Returns a salted and hashed password using the provided hash.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password the password to be hashed.
     * @param salt     a 16 bytes salt, ideally obtained with the getNextSalt method. (Changed to 32).
     *
     * @return the hashed password with a pinch of salt.
     */
    public byte[] saltedHash(char[] password, byte[] salt){
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, key_length);
        Arrays.fill(password, Character.MIN_VALUE);

        try{
            return skf.generateSecret(spec).getEncoded();
        }
        catch(InvalidKeySpecException ikse){
            GUI.launchMessage(2, "Error de encriptación", "");
            throw new AssertionError("Error while hashing a password: " + ikse.getMessage(), ikse);
            
        }
        finally{
            spec.clearPassword();
        }
    }

    /**
     * Returns true if the given password and salt match the hashed value, false otherwise.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password     the password to check
     * @param salt         the salt used to hash the password
     * @param expectedHash the expected hashed value of the password
     *
     * @return true if the given password and salt match the hashed value, false otherwise
     */
    public boolean checkPassword(char[] password, byte[] salt, byte[] expectedHash){
        byte[] passHash = saltedHash(password, salt);
        Arrays.fill(password, Character.MIN_VALUE);

        if(passHash.length != expectedHash.length) return false;

        for(int i = 0; i < passHash.length; i++){
            if(passHash[i] != expectedHash[i]) return false;
        }
        return true;
    }

    /**
     * Generates a random password of a given length, using letters and digits.
     *
     * @param length the length of the password
     *
     * @return a random password
     */
    public static String generateRandomPassword(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int newChar = RANDOM.nextInt(62);
            if (newChar <= 9) {
                sb.append(String.valueOf(newChar));
            } else if (newChar < 36) {
                sb.append((char) ('a' + newChar - 10));
            } else {
                sb.append((char) ('A' + newChar - 36));
            }
        }
        
        return sb.toString();
    }
}
