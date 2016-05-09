package com.jackalhan.ualr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by txcakaloglu on 5/4/16.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       //http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login");
        http.authorizeRequests().antMatchers("/css/**").permitAll().antMatchers("/img/**").permitAll().antMatchers("/js/**").permitAll().anyRequest()
                .fullyAuthenticated().and()
                .formLogin().loginPage("/login")
                .failureUrl("/login?error").permitAll().and().logout().permitAll();
      /* http
                .authorizeRequests()
                .antMatchers("/resources*//**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();*/
    }

    @Configuration
    protected static class AuthenticatedConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            //test-server.ldif is a test file LDAP servers can use LDIF (LDAP Data Interchange Format) files to exchange user data. This makes it easy to pre-load demonstration data.
            auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups").contextSource().ldif("classpath:test-server.ldif");
        }
    }


}
