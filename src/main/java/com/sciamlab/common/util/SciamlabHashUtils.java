package com.sciamlab.common.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @author SciamLab
 *
 */

public class SciamlabHashUtils {

	/*
	 * Base64
	 */
    public static byte[] base64ToByte(String data) {
        return Base64.decodeBase64(data);
    }

    public static String byteToBase64(byte[] data) {
        return new String(Base64.encodeBase64(data));
    }

    /*
     * HEX
     */
    public static byte[] hexToByte(String data) throws DecoderException {
        return Hex.decodeHex(data.toCharArray());
    }

    public static String byteToHex(byte[] data) {
        return new String(Hex.encodeHex(data));
    }

    /*
     * HASHING
     */
    public static String sha1base64(String request) {
        byte[] digest = DigestUtils.sha1(request);
        return new String(byteToBase64(digest));
    }
    public static String sha256base64(String request) {
        byte[] digest = DigestUtils.sha256(request);
        return new String(byteToBase64(digest));
    }
    
    public static String md5base64(String request) {
        byte[] digest = DigestUtils.md5(request);
        return new String(byteToBase64(digest));
    }
    public static String md5hex(String request) {
        byte[] digest = DigestUtils.md5(request);
        return new String(byteToHex(digest));
    }
    
    
//    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException, IOException {
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        digest.reset();
//        digest.update(base64ToByte(salt));
//        return byteToBase64(digest.digest(password.getBytes("UTF-8")));
//    }

      public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
    	  System.out.println(md5hex("qwerty"));
      }
      

}
