package util;

import org.springframework.stereotype.Service;

@Service
public class PanBankIdUtil
{

    public String getBankId( final String pan )
    {
        String replace = pan.replace( "-", "" );
        return replace.substring( 0, 6 );

    }

}
