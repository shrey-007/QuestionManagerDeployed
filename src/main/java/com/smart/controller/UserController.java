package com.smart.controller;

import com.smart.entities.Question;
import com.smart.entities.QuestionExplaination;
import com.smart.entities.User;
import com.smart.helper.LoggedUserManager;
import com.smart.helper.Message;
import com.smart.model.QuestionExplainationRepository;
import com.smart.model.QuestionRepository;
import com.smart.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionExplainationRepository questionExplainationRepository;

    @RequestMapping("/dashboard")
    public String dashboard(Model model, HttpSession session, Authentication authentication){
        return "dashboard";
    }

    @RequestMapping("/addQuestion")
    public String addQuestion(Model model, HttpSession session){
        model.addAttribute("title","Add Question");
        //send user to view
        //add a blank object
        model.addAttribute("question",new Question());
        return "addQuestion";
    }

    @PostMapping("/saveQuestion")
    public String saveQuestion(Model model, HttpSession session, @ModelAttribute Question question, BindingResult bindingResult, @RequestParam("fileName")MultipartFile file,@RequestParam("imageName")MultipartFile image,@RequestParam("notes") String notes){
        try{
            //get user
            User user=(User) model.getAttribute("user");

            System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"+file+"ewcsdvsc0"+image);
            //uploading file
            if(!file.isEmpty()){
                question.setFileName(file.getOriginalFilename());
                File savedFile=new ClassPathResource("static/files").getFile();
                Path path=Paths.get(savedFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111"+file);
            }
            //uploading image
            if(!image.isEmpty()){
                question.setImageName(image.getOriginalFilename());
                File savedFile=new ClassPathResource("static/images").getFile();
                Path path=Paths.get(savedFile.getAbsolutePath()+File.separator+image.getOriginalFilename());
                Files.copy(image.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("22222222222222222222222222222222222222222222222222222222222222222222222222222222222222"+image);
            }
            question.setUser(user);

            //add question to user's list
            user.getQuestions().add(question);
            //update user in database
            userRepository.save(user);
            //saving the explaination of the question in mongo db
            questionExplainationRepository.save(new QuestionExplaination(question.getQid(),notes));
            //we can do the same by making QuestionRepository
            session.setAttribute("message",new Message("Added Successfully","success"));
            return "addQuestion";
        }
        catch (Exception e){
            model.addAttribute("message",new Message("Error Adding Question","danger"));
            System.out.println(e.getMessage());
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
        return "addQuestion";

    }


    //{page} is a dynamic variable which denotes ki pagination mai currently konse page pr ho
    @GetMapping("/showQuestions/{page}")
    public String showQuestions(@PathVariable("page") Integer page, Model model,HttpSession session){
        //get the user
        User user=(User) model.getAttribute("user");

        //this pageable contains information of current page and no. of questions per page
        Pageable pageable =PageRequest.of(page,3);
        Page<Question> questionList=questionRepository.findQuestionsByUser(user.getId(),pageable);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(questionList);
        model.addAttribute("questions",questionList);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",questionList.getTotalPages());
        return "showQuestions";
    }

    @RequestMapping("/question/{qid}")
    public String showContactDetails(@PathVariable("qid") Integer qid, Model model,HttpSession session){
        //fetch current user
        User user=(User) model.getAttribute("user");

        //fetch question by qid
        Optional<Question> questionOptional=this.questionRepository.findById(qid);
        Question question=questionOptional.get();
        model.addAttribute("question",question);

        //fetch the explaination of the question using qid
        QuestionExplaination questionExplaination=questionExplainationRepository.findByQid(qid);
        model.addAttribute("questionExplaination",questionExplaination);

        return "showContactDetail";
    }


    //for changing password
    @GetMapping("/profile")
    public String openProfile(HttpSession session,Model model){
        return "profile";
    }

    @PostMapping("/changePassword")
    public String changePassword(HttpSession session,Model model,@RequestParam("newPassword") String newPassword){

        User user=(User) model.getAttribute("user");

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        //send message that password is updated
        session.setAttribute("message",new Message("Your Password has been changed","alert-success"));
        return "profile";
    }

    @RequestMapping("/toDo")
    public String toDo(Model model,HttpSession session,@RequestParam("toDo") String newTask){
        //get the user
        User user=(User) model.getAttribute("user");

        //add the task
        String tasks=user.getToDo();
        if(tasks==null){tasks=newTask;}
        else{tasks=tasks+newTask;}

        user.setToDo(tasks);

        userRepository.save(user);
        return "dashboard";
    }


}
