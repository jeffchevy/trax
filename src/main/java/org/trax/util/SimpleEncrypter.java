package org.trax.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class SimpleEncrypter
{
    private static String algorithm = "DESede";


    public static void main(String[] args) throws Exception
    {
        String input = "4";
        System.out.println("Entered: " + input);
        
        String passcode = getPassCode(); 
        String hexstring = encrypt(input, passcode);
        System.out.println("hex string="+hexstring+ "  passcode="+passcode);
        System.out.println("Recovered: " + decrypt(hexstring, passcode));
    }

    public static String getPassCode() throws NoSuchAlgorithmException
    {
        Key key = KeyGenerator.getInstance(algorithm).generateKey();
        return byteArrayToHexString(key.getEncoded()); 
    }
    
    public static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    
    public static String byteArrayToHexString(byte in[])
    {
        byte ch = 0x00;
        int i = 0;
        if (in == null || in.length <= 0)
            return null;

        String pseudo[] =
        { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        StringBuffer out = new StringBuffer(in.length * 2);

        while (i < in.length)
        {
            ch = (byte) (in[i] & 0xF0); // Strip off high nibble
            ch = (byte) (ch >>> 4);
            // shift the bits down
            ch = (byte) (ch & 0x0F);
            // must do this is high order bit is on!
            out.append(pseudo[ch]); // convert the nibble to a String Character
            ch = (byte) (in[i] & 0x0F); // Strip off low nibble
            out.append(pseudo[ch]); // convert the nibble to a String Character
            i++;
        }
        String rslt = new String(out);
        return rslt;
    }

    public static String encrypt(String input, String passcode) throws Exception
    {
        Cipher cipher = Cipher.getInstance(algorithm);
        byte[] bytes = hexStringToByteArray(passcode);
        cipher.init(Cipher.ENCRYPT_MODE,  new SecretKeySpec(bytes, "DESede"));
        byte[] inputBytes = input.getBytes();
        return byteArrayToHexString(cipher.doFinal(inputBytes));
    }

    public static String decrypt(String encryptionBytes, String passcode)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance(algorithm);
        byte[] bytes = hexStringToByteArray(passcode);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(bytes, "DESede"));
        byte[] recoveredBytes = cipher.doFinal(hexStringToByteArray(encryptionBytes));
        String recovered = new String(recoveredBytes);
        return recovered;
    }
}