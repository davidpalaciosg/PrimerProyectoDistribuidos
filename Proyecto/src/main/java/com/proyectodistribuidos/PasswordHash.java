package com.proyectodistribuidos;

import java.security.*;  
import java.math.BigInteger;  
import java.security.MessageDigest;  
import java.nio.charset.StandardCharsets;  
import javax.crypto.spec.PBEKeySpec;   
import javax.crypto.SecretKeyFactory;  
import java.security.spec.InvalidKeySpecException;  

public class PasswordHash {
    public String crearPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{  
        // number of iteration used  
        int itr = 500;  
        char[] charArr = password.toCharArray();  
        byte[] saltArr = obtenerSalt();  
           
        PBEKeySpec pbeSpec = new PBEKeySpec(charArr, saltArr, itr, 64 * 8);  
          
        // Usando el algoritmo PBKDF2WithHmacSHA1 para hashing  
        SecretKeyFactory secKeyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");  
        byte[] hashArr = secKeyFact.generateSecret(pbeSpec).getEncoded();  
        return itr + ": " + toHex(saltArr) + ":" + toHex(hashArr);  
    }  

    // Salt   
    public byte[] obtenerSalt() throws NoSuchAlgorithmException{  
        SecureRandom secRand = SecureRandom.getInstance("SHA1PRNG");  
        byte[] saltArr = new byte[16];  
        secRand.nextBytes(saltArr);  
        return saltArr;  
    }  

    //Convertir en hexadecimal
    public String toHex(byte[] arr) throws NoSuchAlgorithmException  
    {  
        BigInteger bInt = new BigInteger(1, arr);  
        String hexStr = bInt.toString(16);  
        int paddingLen = (arr.length * 2) - hexStr.length();  
        if(paddingLen > 0)  
        {  
            return String.format("%0"  + paddingLen + "d", 0) + hexStr;  
        }  
        else  
        {  
            return hexStr;  
        }  
    }  


}