package com.ws.sep.acquirer.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

    @Override
    protected void configure( HttpSecurity http ) throws Exception
    {
        http.csrf().disable()

                .requestMatchers().antMatchers( "/oauth/token" )

                .and()

                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )

                .and().authorizeRequests()

                .antMatchers( HttpMethod.OPTIONS ).permitAll()

                .anyRequest().authenticated();

    }

}
