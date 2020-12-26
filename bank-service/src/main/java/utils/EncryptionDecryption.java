package utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDecryption
{

    /**
     *
     */
    private static final int NUMBER = 256;

    private static final String algorithm = "AES/CBC/PKCS5Padding";

    private static SecretKey generateKey( int n ) throws NoSuchAlgorithmException
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance( "AES" );
        keyGenerator.init( n );
        SecretKey key = keyGenerator.generateKey();
        return key;

    }


    private static IvParameterSpec generateIv()
    {
        byte[] iv = new byte[ 16 ];
        new SecureRandom().nextBytes( iv );
        return new IvParameterSpec( iv );

    }


    private static IvParameterSpec generateIv( byte[] params )
    {

        return new IvParameterSpec( params );

    }


    private static String encrypt( String algorithm, String input, SecretKey key, IvParameterSpec iv ) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance( algorithm );
        cipher.init( Cipher.ENCRYPT_MODE, key, iv );
        byte[] cipherText = cipher.doFinal( input.getBytes() );
        return Base64.getEncoder().encodeToString( cipherText );

    }


    private static String decrypt( String algorithm, String cipherText, SecretKey key, IvParameterSpec iv ) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance( algorithm );
        cipher.init( Cipher.DECRYPT_MODE, key, iv );
        byte[] plainText = cipher.doFinal( Base64.getDecoder().decode( cipherText ) );
        return new String( plainText );

    }


    public static String encryptString( String input )
    {
        try
        {

            SecretKey generateKey = EncryptionDecryption.generateKey( NUMBER );
            IvParameterSpec generateIv = EncryptionDecryption.generateIv();

            String encrypt = EncryptionDecryption.encrypt( EncryptionDecryption.algorithm, input, generateKey, generateIv );

            String IvParameterSpecsString = Base64.getEncoder().encodeToString( generateIv.getIV() );
            String keyString = Base64.getEncoder().encodeToString( generateKey.getEncoded() );

            String encryptedValue = IvParameterSpecsString + keyString + encrypt;

            return encryptedValue;
        }
        catch ( Exception e )
        {
            // TODO: log

            return null;
        }

    }


    public static String decryptString( String input )
    {
        try
        {

            String ivParameterString = input.substring( 0, 24 );
            String keyString = input.substring( 24, 68 );
            String value = input.substring( 68 );

            byte[] decodedKey = Base64.getDecoder().decode( keyString );
            byte[] decodedIvParStr = Base64.getDecoder().decode( ivParameterString );
            SecretKey originalKey = new SecretKeySpec( decodedKey, 0, decodedKey.length, "AES" );
            IvParameterSpec ivParameters = EncryptionDecryption.generateIv( decodedIvParStr );

            String decrypt = EncryptionDecryption.decrypt( EncryptionDecryption.algorithm, value, originalKey, ivParameters );

            return decrypt;

        }
        catch ( Exception e )
        {
            // TODO: log

            return null;
        }

    }

}
