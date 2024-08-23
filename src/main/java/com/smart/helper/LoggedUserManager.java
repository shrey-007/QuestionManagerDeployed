package com.smart.helper;

import com.smart.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;

public class LoggedUserManager {

    public static String getEmailOfLoggedUser(Authentication authentication){

        System.out.println("start");
        if(authentication instanceof OAuth2AuthenticationToken){
            System.out.println("auth2");
            OAuth2User oauth2User= (OAuth2User)authentication.getPrincipal();
            return oauth2User.getAttribute("email").toString();
        }
        else{
            System.out.println("normal");
            return authentication.getName();
        }
    }
}
