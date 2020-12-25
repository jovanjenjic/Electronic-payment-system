package util;

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

public class EncryptDecrypt
{

    /**
     *
     */
    private static final String algorithm = "AES";

    private static final String key = "*QS;W;Mt<Sy-s?pSAu7!sapZrFNR.+32";

    public static String encrypt( String input )
    {
        try
        {

            SecretKeySpec secretKeySpec = new SecretKeySpec( key.getBytes(), algorithm );
            Cipher cipher = Cipher.getInstance( algorithm );

            cipher.init( Cipher.ENCRYPT_MODE, secretKeySpec );

            byte[] encrypted = cipher.doFinal( input.getBytes() );

            return Base64.getEncoder().encodeToString( encrypted );
        }
        catch ( Exception e )
        {
            return null;
        }

    }


    public static String decrypt( String input )
    {

        try
        {

            SecretKeySpec secretKeySpec = new SecretKeySpec( key.getBytes(), algorithm );
            Cipher cipher = Cipher.getInstance( algorithm );

            cipher.init( Cipher.DECRYPT_MODE, secretKeySpec );

            byte[] decrypted = cipher.doFinal( Base64.getDecoder().decode( input ) );

            return new String( decrypted );
        }
        catch ( Exception e )
        {
            return null;
        }

    }


    public static SecretKey generateKey( int n ) throws NoSuchAlgorithmException
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance( "AES" );
        keyGenerator.init( n );
        SecretKey key = keyGenerator.generateKey();
        return key;

    }


    public static IvParameterSpec generateIv()
    {
        byte[] iv = new byte[ 16 ];
        new SecureRandom().nextBytes( iv );
        return new IvParameterSpec( iv );

    }


    public static IvParameterSpec generateIv( byte[] params )
    {

        return new IvParameterSpec( params );

    }


    public static String encrypt( String algorithm, String input, SecretKey key, IvParameterSpec iv ) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance( algorithm );
        cipher.init( Cipher.ENCRYPT_MODE, key, iv );
        byte[] cipherText = cipher.doFinal( input.getBytes() );
        return Base64.getEncoder().encodeToString( cipherText );

    }


    public static String decrypt( String algorithm, String cipherText, SecretKey key, IvParameterSpec iv ) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance( algorithm );
        cipher.init( Cipher.DECRYPT_MODE, key, iv );
        byte[] plainText = cipher.doFinal( Base64.getDecoder().decode( cipherText ) );
        return new String( plainText );

    }

}
