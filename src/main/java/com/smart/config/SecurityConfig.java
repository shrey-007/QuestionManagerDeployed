package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

    // spring security UserDetailsService ko use krta hai jabhi bhi apan login krte hai, us service ke paas ek method hota hai
    // loadUserByUserName us method ko use krega, user ko load krane ke liye. Fir loaded user and humara user ka password match
    // krega , if it matches toh login kra dega


    // If we want ki spring user ki details databse se le toh hum ye krege
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize->{
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });
        httpSecurity.formLogin(formLogin->{
           formLogin.loginPage("/login");
            // you don't have to create a controller for this /authenticate, ye bas ye bata ne ke liye hai ki login form ka
            // ke action mai /authenticate dala hai, toh form yaha preccessed hoga khud se spring krega ki credentials sahi hai ya nhi, if yes toh
            // toh page /user/dashboard pr le jaaega, but url same rahega /authenticate hi. If you want ki url bhi change ho
            // toh defaultSuccessURl("/user/dashboard") kro
            // If credentials are wrong , toh url /login?error=true ho jaaega toh login page mai check krlo ki th:if${param.error}
            // true hai toh kya message dikhana hai
           formLogin.loginProcessingUrl("/authenticate");
           formLogin.successForwardUrl("/user/dashboard");

           formLogin.usernameParameter("email");
           formLogin.passwordParameter("password");
        });

        httpSecurity.logout(logoutForm->{
           logoutForm.logoutUrl("/logout");
        });

        // oauth2
        httpSecurity.oauth2Login(oauth->{
            // isi login page mai hi sign in with google ka button hai
            oauth.loginPage("/login");
            oauth.successHandler(oAuthAuthenticationSuccessHandler);
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
