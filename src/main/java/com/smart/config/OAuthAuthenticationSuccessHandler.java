package com.smart.config;

import com.smart.entities.Providers;
import com.smart.entities.User;
import com.smart.model.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger=LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepository userRepository;

    // this method will run when google se successful login ho jaaye
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // get the information of the user
        DefaultOAuth2User user=(DefaultOAuth2User)authentication.getPrincipal();

        String email=user.getAttribute("email").toString();
        String name=user.getAttribute("name").toString();

        User user1=new User();
        user1.setEmail(email);
        user1.setName(name);
        // password store krne ki need nhi hai

        user1.setProvider(Providers.GOOGLE);
        user1.setEnabled(true);
        user1.setEmailVerified(true);
        user1.setProviderUserId(user.getName());

        logger.info("got all details of the user");


        // if the user email is already in database, means ya toh ye login vala case hai ya fir user ne normal registration kr tha
        if (userRepository.findByEmail(email)!=null){
            logger.info("login");
            response.sendRedirect("/user/dashboard");
        }
        else{
            // means ki ye signup ka case hai
            logger.info("saving the user");
            // save the user
            userRepository.save(user1);
            response.sendRedirect("/user/dashboard");
        }
        logger.info("onAuthenticationSuccess");
    }


}
