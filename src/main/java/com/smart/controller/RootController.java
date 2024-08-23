package com.smart.controller;

import com.smart.entities.User;
import com.smart.helper.LoggedUserManager;
import com.smart.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {
    Logger logger= LoggerFactory.getLogger(RootController.class);

    @Autowired
    private UserRepository userRepository;

    // this function will run everytime whenever a request is hit
    @ModelAttribute
    public void sendLoggedUserInformationToView(Model model, Authentication authentication){
        if(authentication==null){return;}
        // find the email of the user jisne login kra
        String email= LoggedUserManager.getEmailOfLoggedUser(authentication);
        logger.info(email+"------------------------------------------------------BEFORE CONTROLLER----------------------------------------------");
        // get the user from email
        User user=userRepository.findByEmail(email);
        logger.info("user is {} ",user);
        model.addAttribute("user",user);
    }

}
