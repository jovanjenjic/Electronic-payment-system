package com.ws.sep.seller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

    @Autowired
    private SellerDetailServiceImplementation sellerDetailServiceImplementation;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter()
    {
        return new JwtAuthenticationFilter();

    }


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();

    }


    @Override
    public void configure( AuthenticationManagerBuilder authenticationManagerBuilder ) throws Exception
    {
        authenticationManagerBuilder.userDetailsService( this.sellerDetailServiceImplementation ).passwordEncoder( passwordEncoder() );

    }


    @Override
    protected void configure( HttpSecurity http ) throws Exception
    {
        http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint( unauthorizedHandler ).and().sessionManagement()
                .sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and().authorizeRequests()

                .antMatchers( "/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js" ).permitAll()

                .antMatchers( "/pay-pal-service/**" ).hasRole( "PAYPAL" )

                .antMatchers( "/bank-service/**" ).hasRole( "BANK" )

                .antMatchers( "/bitcoin-service/**" ).hasRole( "BITCOIN" )

                .antMatchers( "/api/auth/signup" ).hasRole( "ADMIN" )

                .antMatchers( "/api/auth/**" ).permitAll()

                .antMatchers( "/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability" ).permitAll()

                .anyRequest().authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore( jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class );

    }


    @Bean( BeanIds.AUTHENTICATION_MANAGER )
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();

    }

}
