/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月1日 下午6:29:12
 * @version V1.0
 */

package com.iqb.consumer.common.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class MessageDigestUtil {
    private static final String HMAC_SHA512_ALGORITHM = "HmacSHA512";
    private static final Logger log = LoggerFactory.getLogger(MessageDigestUtil.class);

    /**
     * MD5 hash function to be "cryptographically secure", the result is 32 HEX characters.
     * <ol>
     * <li>Given a hash, it is computationally infeasible to find an input that produces that hash</li>
     * <li>Given an input, it is computationally infeasible to find another input that produces the
     * same hash</li>
     * </ol>
     * 
     * @param plainText
     * @return the MD5 of the input text
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5(byte[] plainText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String md5 = new BigInteger(1, messageDigest.digest(plainText)).toString(16);
        if (md5.length() < 32) {
            md5 = "0" + md5;
        }

        messageDigest.reset();

        if (log.isDebugEnabled()) {
            log.debug(new String(plainText, "utf-8") + "'s MD5: " + md5);
        }

        return md5;
    }

    /**
     * MD5 hash function to be "cryptographically secure", the result is 32 HEX characters.
     * <ol>
     * <li>Given a hash, it is computationally infeasible to find an input that produces that hash</li>
     * <li>Given an input, it is computationally infeasible to find another input that produces the
     * same hash</li>
     * </ol>
     * 
     * @param plainText
     * @param salt
     * @return the MD5 of the input text
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5(byte[] plainText, byte[] salt) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(salt);
        String md5 = new BigInteger(1, messageDigest.digest(plainText)).toString(16);
        if (md5.length() < 32) {
            md5 = "0" + md5;
        }

        messageDigest.reset();

        if (log.isDebugEnabled()) {
            log.debug(new String(plainText, "utf-8") + "[salt=" + new String(salt, "utf-8")
                    + "] 's MD5: " + md5);
        }

        return md5;
    }

    /**
     * SHA-512 hash function to be "cryptographically secure", the result is 128 HEX characters
     * <ol>
     * <li>Given a hash, it is computationally infeasible to find an input that produces that hash</li>
     * <li>Given an input, it is computationally infeasible to find another input that produces the
     * same hash</li>
     * </ol>
     * 
     * @param plainText
     * @param salt
     * @return the SHA-512 of the input text
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String sha512(byte[] plainText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        String sha512 = new BigInteger(1, messageDigest.digest(plainText)).toString(16);
        if (sha512.length() < 128) {
            sha512 = "0" + sha512;
        }

        messageDigest.reset();

        if (log.isDebugEnabled()) {
            log.debug(new String(plainText, "utf-8") + "'s SHA512: " + sha512);
        }

        return sha512;
    }

    /**
     * Computes RFC 2104-compliant HMAC signature.
     * 
     * @param data The data to be signed
     * @param secretKey the secretKey for signature
     * @return The base64-encoded RFC 2104-compliant HmacSHA512 signature.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String rfc2104HmacSha512(String data, String secretKey) {
        String result;
        try {
            // get an hmac_sha512 key from the raw key bytes
            SecretKeySpec signingKeySpec =
                    new SecretKeySpec(secretKey.getBytes(Charset.forName("utf-8")), HMAC_SHA512_ALGORITHM);

            // get an hmac_sha512 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA512_ALGORITHM);
            mac.init(signingKeySpec);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes(Charset.forName("utf-8")));

            // base64-encode the hmac
            result = new String(Base64.encodeBase64(rawHmac), Charset.forName("utf-8"));

            if (log.isDebugEnabled()) {
                log.debug(data + "'s HmacSHA512: " + result);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Calculate HmacSHA512 failed: " + e.getMessage(), e);
        }

        return result;
    }

    public static String calculateSignature(String httpMethod, String date, String resourcePath, String secretKey) {
        // calculate the signature
        // StringToSign = HTTP-Method + "\n" + Date + "\n" + resourcePath + "\n";
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(httpMethod).append("\n");
        stringToSign.append(date).append("\n");
        stringToSign.append(resourcePath).append("\n");
        if (log.isDebugEnabled()) {
            log.debug("stringToSign = " + stringToSign.toString());
        }
        String calculatedSignature = rfc2104HmacSha512(stringToSign.toString(), secretKey);
        return calculatedSignature;
    }

    /**
     * An MD5 hash help function to be "cryptographically secure", the result is 32 HEX characters.
     * <p>
     * It is purpose to be compatible with {@literal Spring's}
     * {@link MessageDigestPasswordEncoder#encodePassword(java.lang.String, java.lang.Object)},
     * which merges the password with salt as: <br />
     * <code>(password + "{" + salt.toString() + "}" </code>
     * 
     * <ol>
     * <li>Given a hash, it is computationally infeasible to find an input that produces that hash</li>
     * <li>Given an input, it is computationally infeasible to find another input that produces the
     * same hash</li>
     * </ol>
     * 
     * @param plainText
     * @param salt
     * @return the MD5 of the input text
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String springMd5PasswordEncode(String password, Object salt) {
        PasswordEncoder encoder = new Md5PasswordEncoder();

        String md5 = encoder.encodePassword(password, salt);

        if (log.isTraceEnabled()) {
            log.trace(password + "[salt=" + salt + "] 's MD5: " + md5);
        }
        return md5;
    }

    /**
     * calculate the md5 of the file as linux command 'md5sum file'.
     * 
     * @param file the while path to the file
     * @return
     */
    public static String md5File(String file) {
        MessageDigest md = null;
        InputStream fis = null;
        StringBuilder hexString = null;

        try {
            md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);

            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }

            byte[] mdbytes = md.digest();
            // convert the byte to hex format
            hexString = new StringBuilder();
            for (int i = 0; i < mdbytes.length; i++) {
                String hex = Integer.toHexString(0xff & mdbytes[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

        } catch (Exception ex) {
            log.warn("md5File: error to close inputstream of file: {}", file, ex);
        } finally {
            try {
                fis.close();
            } catch (Exception ex) {
                log.warn("md5File: error to close inputstream of file: {}", file, ex);
            }
        }

        return hexString.toString();
    }

    public static void main(String[] args) {
        // String p = "jbf123123";
        // String salt = "jbf001";
        // System.out.println("md5 encode = " + MessageDigestUtil.springMd5PasswordEncode(p, salt));
        System.out.println(rfc2104HmacSha512("12301123", "12322"));
    }
}
