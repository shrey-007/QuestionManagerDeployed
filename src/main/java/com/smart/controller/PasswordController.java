package com.smart.controller;

import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.model.UserRepository;
import com.smart.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class PasswordController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    Random random=new Random(1000);
    @GetMapping("/forgotPassword")
    public String forgotPassword(HttpSession session, Model model) {

        return "forgotPassword";
    }

    @PostMapping("/sendOTP")
    public String sendOTP(@RequestParam("email") String email,HttpSession session){

        //generate a random otp
        int otp=random.nextInt(99999);

        //code to send otp to mail
        String subject="OTP for verification from Shrey's website";
        //you can use div,style also in message to send a good message
        String message="<h1>Your OTP for verification is </h1>"+otp;
        String to=email;

        boolean flag=this.emailService.sendEmail(subject,message,to);

        if(flag){
            //means ki otp jaa chuka hai toh ab user ko otp daalna hai jo usko mail pr aaya hai
            session.setAttribute("otp",otp);
            session.setAttribute("email",email);
            return "verifyOTP";
        }
        else{
            session.setAttribute("message",new Message("Please enter a valid registerd email","alert-danger"));
            return "forgotPassword";

        }

    }


    @PostMapping("/verify-otp")
    public String varifyOTP(@RequestParam("otp")int OtpEnterdByUser,HttpSession session,Model model){

        int OtpGeneratedByMail=(int)session.getAttribute("otp");
        String emailEnteredByUser=(String) session.getAttribute("email");

        if(OtpEnterdByUser==OtpGeneratedByMail){

            //means it is valid user toh ise password change krne do, toh uske liye pehle user fetch kro
            User user=this.userRepository.findByEmail(emailEnteredByUser);

            if(user==null){
                //means ki user ne jo email daali vo registered email nhi hai coz usse koi bhi user fetch hokar nhi aa rha
                session.setAttribute("message",new Message("The email you entered is not a registerd email.Either sign up or enter registered email.","alert-danger"));
                return "forgotPassword";
            }
            else{
                //means ki otp bhi sahi hai email bhhi sahi hai and user bhi aa gya hai
                //but ye page tabhi khulega jab user attribute ko session mai daala hoga
                session.setAttribute("currentUser",user);
                model.addAttribute("user",user);
                return "profile";
            }

        }
        else{
            session.setAttribute("message",new Message("You have entered wrong OTP","alert-danger"));
            return "verifyOTP";
        }




    }

}
