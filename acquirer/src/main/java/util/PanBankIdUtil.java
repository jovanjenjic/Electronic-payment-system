package util;

import org.springframework.stereotype.Service;

@Service
public class PanBankIdUtil
{

    public static final String PCC_URL = "https://localhost:8085/check";

    public String getBankId( final String pan )
    {
        String replace = pan.replace( "-", "" );
        return replace.substring( 0, 6 );

    }

}
