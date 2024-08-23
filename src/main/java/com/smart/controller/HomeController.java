package com.smart.controller;

import com.smart.entities.Providers;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title","Question Manager-home");
        return "home";
    }



    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title","Question Manager-Signup");
        //yaha se blank fields form mai jaaegi and jab apan form mai kuch daalege toh voh fields idhar aa jaaegi.
        //ye map kr rha hai form ko controller se.
        //isse hoga ye ki agar koi error aayi validation mai user ko vaapis se saare input nhi dene padega
        //vaha har input par th:value likha hai.
        model.addAttribute("user",new User());
        return "signup";
    }

    @PostMapping("/do_register")
    //since form mai apan ne th:object="user" liya hai isliye yaha ModelAttribute("user") liya
    //form mai jitni fields hai vo saari User class se map ho jaaegi by ModelAttribute coz name same hai dono jagah
    //checkbox is not property of user toh usko alag se request param mai liya hai.
    //and yaha se data vaapis form mai bhejne ke liye model object liya hai
    //validation mai jo bhi error hoga voh bindingResult ke object mai aa jaaega,and error ka message bhi usi mai hoga.vo error ka msg and message class dono alag hai
    public String register(@Valid @ModelAttribute("user") User user,BindingResult bindingResult, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model, HttpSession session){
       try{
           System.out.println(agreement);
           System.out.println(user);
           if(!agreement){
               throw new Exception("You have not agreed terms and conditions");
           }
           if(bindingResult.hasErrors()){
               //since error aayi hai toh puraana vala user hi bhej do
               model.addAttribute("user",user);
               return "signup";
           }

           // user ka password database mai encode krke save krna hai
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           // since user ne khud se login kiya hai, tabhi is controller pr aaye hai toh provider mai self daaldo
           user.setProvider(Providers.SELF);
           user.setProviderUserId(user.getName());

           // also check ki esa toh nhi ki isi user ne google(oauth) se login krke rakha hai toh duplicate entry ho jaaegi iski
           // and exception aa jaaegi jiuki email uniques hai
           if(userRepository.findByEmail(user.getEmail())!=null){
               // means it is duplicate entry
               throw new Exception("This email is already registered");
           }

           //save user in database
           User result=this.userRepository.save(user);

           //is baar database mai user save ho gya toh form mai vaapis se details dikhaane ki need nhi hai, simply new object bhejo.
           model.addAttribute("user",new User());
           //since user register ho gya toh success msg bhej do
           session.setAttribute("message",new Message("Successfully Registered.Please go to Login Page","alert-success"));
       }
       catch (Exception e){
          e.printStackTrace();
          //yaha tab aaege jab error aaegi
           //jo user ka data aaya hai  usi ko vaapis bhej diya, toh user ko vaapis form nhi bharna padega.
          model.addAttribute("user",user);
          //session attribute liya hi isliye hai taaki message bhej sako
          session.setAttribute("message",new Message("Something went wrong."+e.getMessage(),"alert-danger"));
       }
        return "signup";
    }


    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("user",new User());
        return "login";
    }


    @RequestMapping("/logout")
    public String logout(Model model,HttpSession session){
        session.removeAttribute("currentUser");
        return "home";
    }

}
