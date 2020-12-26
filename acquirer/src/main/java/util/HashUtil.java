package util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashUtil
{

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String generateHash( String toBeHashed )
    {
        return this.passwordEncoder.encode( toBeHashed );

    }


    public Boolean verifyHash( String candidate, String password )
    {
        return this.passwordEncoder.matches( candidate, password );

    }

}
