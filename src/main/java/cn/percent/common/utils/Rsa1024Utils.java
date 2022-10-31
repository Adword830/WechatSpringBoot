package cn.percent.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhangpengju
 * Date: 2021/11/18
 * Time: 16:03
 * Description:
 */
public class Rsa1024Utils {

    public static final String PRIVATE_KEY="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANWwc/S1T1VQXJmJS3Z0Xt3edhL5" +
            "DtFPkGDy2hLpfKKo1FwUP/ZP4UmYDe1HLgozFE+ftasQJwOVlBFcTyicmHPla4/FfPVgUW3X2+cj56eJddbB+6dIUgJrWD/l/zVgf5vXP/Cr" +
            "Iz8NBimgv7J83Bp7QGpSFTvVKaVMnvIzryfFAgMBAAECgYBSlLmLNVJuZvsSPtPR+FD9L060MFoAm93xCYkrS95lwhcnao/5cY9f8IpZS2vId0r" +
            "/Jnp/nGNnaDPrrZ/N0w667nkrklyFMgOcHftq6fiIaM+XF7AwmSAw5fB211plF/6nENr/+WdnHwkKIY0VA4NGr0IYo8YB7w5cLtvoY1yYNQJBAO8T" +
            "1Vh5hpvulTqTfRYfKRsTNG+CegdpUFDtWnwkqghwdBHhsWEj1SFPHR/3BviM5KM1qp39PvBOP1vJqwRoMNsCQQDk0JOWGcPo5b2e397V15dQJyP9/+u" +
            "7TmuXBdIJ6H0RXi1tGzQ3iX2v6mzQcPLpiTeJYBHZzzb5Akz46IfpiJvfAkAIW4YiwKTPAKkg0hUWV+KE+sfjegkwByNyoMl/+3wiLWQxV9VukuUZc/" +
            "2Np4IL4EkB15duTHMNzX5zg/pBfi7vAkEArHJFyKpCbBQ5hLNUTfXdpBrblWJ7Vv8TNw677Zu6JozDz6bhA7orb5lNyQpXCIrRwEcXLQBufdyuBim5xNeg" +
            "IQJAWWfmaGE0PPQx1sUlQXJ+ghx+9CIkoLq6YJHd29/2jzaziItvaAwbBNekzgaMXE/CzzYuFYh2qmPl2A8vISiifA==";

    public static final String PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVsHP0tU9VUFyZiUt2dF7d3nYS+Q7RT5Bg8toS6" +
            "XyiqNRcFD/2T+FJmA3tRy4KMxRPn7WrECcDlZQRXE8onJhz5WuPxXz1YFFt19vnI+eniXXWwfunSFICa1g/5f81YH+b1z/wqyM/DQYpoL+y" +
            "fNwae0BqUhU71SmlTJ7yM68nxQIDAQAB";

    /**
     * 用于封装随机产生的公钥与私钥
     */
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();

    public static void main(String[] args) throws Exception {
        // 生成公钥和私钥
        genKeyPair();
        // 加密字符串
        String message = "123456";
        String messageEn = encrypt(message,PUBLIC_KEY);
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn,PRIVATE_KEY);
        System.out.println("还原后的字符串为:" + messageDe);
    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map 0表示公钥 1表示私钥
        keyMap.put(0,publicKeyString);
        keyMap.put(1,privateKeyString);
    }
    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str  加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) {
        //64位解码加密后的字符串
        byte[] inputByte = new byte[0];
        try {
            inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = null;
        try {
            priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //RSA解密
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, priKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        String outStr = null;
        try {
            outStr = new String(cipher.doFinal(inputByte));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return outStr;
    }

}
