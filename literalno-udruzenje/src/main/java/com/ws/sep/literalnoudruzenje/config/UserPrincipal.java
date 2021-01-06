package com.ws.sep.literalnoudruzenje.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ws.sep.literalnoudruzenje.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    /**
     *
     */
    private static final long serialVersionUID = 1175670991961487821L;


    private Long id;

    private String name;

    private String email;

    @JsonIgnore
    private String password;

    private Long pib;

    private Collection<? extends GrantedAuthority> authorities;


    public UserPrincipal( Long id, String email, String password, Collection< ? extends GrantedAuthority > authorities )
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {

        List< GrantedAuthority > authorities =
                user.getRoles().stream().map( role -> new SimpleGrantedAuthority( role.getName().name() ) ).collect( Collectors.toList() );

        return new UserPrincipal(user.getId(), user.getName(), user.getPassword(), authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
