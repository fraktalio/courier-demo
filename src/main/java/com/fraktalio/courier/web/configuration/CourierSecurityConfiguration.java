package com.fraktalio.courier.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;


@Configuration
@EnableReactiveMethodSecurity
class CourierSecurityConfiguration {

    @Bean
    MapReactiveUserDetailsService authentication() {
        //This is NOT intended for production use (it is intended for getting started experience only)
        var user = User.withDefaultPasswordEncoder()
                .username("courier")
                .password("pass")
                .roles("COURIER")
                .build();
        var manager = User.withDefaultPasswordEncoder()
                .username("manager")
                .password("pass")
                .roles("MANAGER")
                .build();
        return new MapReactiveUserDetailsService(user, manager);
    }
}
