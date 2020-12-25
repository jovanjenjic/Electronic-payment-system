package util;

import java.util.Base64;


import javax.crypto.Cipher;
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

}
