package com.ws.sep.literalnoudruzenje.config;

import com.ws.sep.literalnoudruzenje.model.User;
import com.ws.sep.literalnoudruzenje.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    // overide but email is sent
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException( "Seller with email [" + email + "] not found!" ));

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Long id)
    {
        User user = userRepository.findById(id).orElseThrow( () -> new UsernameNotFoundException( "User with id [" + id + "] not found!" ) );

        return UserPrincipal.create(user);
    }
}
