package com.paymentchain.adminserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    public static class SecuriyPermitAllConfig extends WebSecurityConfigurerAdapter{
//
//        @Override
//        protected void configure(HttpSecurity httpSecurity) throws Exception{
//            httpSecurity
//                    .authorizeRequests()
//                    .anyRequest()
//                    .permitAll()
//                    .and().csrf().disable();
//        }
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests)-> requests
                .anyRequest()
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {

        ArrayList<UserDetails> userDetailsServiceArrayList = new ArrayList<>();

        userDetailsServiceArrayList.add(
                User.withUsername("user")
                .password(bCryptPasswordEncoder.encode("userPass"))
                .roles("USER")
                .build()
        );

        userDetailsServiceArrayList.add(
                User.withUsername("admin")
                .password(bCryptPasswordEncoder.encode("adminPass"))
                .roles("USER", "ADMIN")
                .build()
        );

        return new InMemoryUserDetailsManager(userDetailsServiceArrayList);
    }


}
