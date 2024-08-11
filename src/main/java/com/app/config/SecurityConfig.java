package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf->csrf.disable()) //se debe usar con MVC, en ese caso se deshabilita
                .httpBasic(Customizer.withDefaults()) //se usa con autenticacion basica
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //no se guarda session en memoria
                .authorizeHttpRequests(http->{
                    //configurar los publicos
                    http.requestMatchers(HttpMethod.GET,"/auth/hello").permitAll();
                    //configurar los privados
                    http.requestMatchers(HttpMethod.GET,"/auth/hello-secured").hasAuthority("CREATE");

                    //configurar los no especificados
                    http.anyRequest().denyAll();
                    //http.anyRequest().authenticated(); //se pueden usar ambos pero authenticated() deja pasar si usas tus credenciales
                })
                .build();
    }*/

    // Con anotations
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf->csrf.disable()) //se debe usar con MVC, en ese caso se deshabilita
                .httpBasic(Customizer.withDefaults()) //se usa con autenticacion basica
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //no se guarda session en memoria
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());

        return provider;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        List<UserDetails> userDetailsList = new ArrayList<>();

        userDetailsList.add(User.withUsername("pepito")
                .password("1234")
                .roles("ADMIN")
                .authorities("READ","CREATE")
                .build());

        userDetailsList.add(User.withUsername("conchita")
                .password("1234")
                .roles("USER")
                .authorities("READ")
                .build());

        return new InMemoryUserDetailsManager(userDetailsList);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //NoOpPasswordEncoder solo se debe usar para hacer pruebas
        return NoOpPasswordEncoder.getInstance();
    }
}
