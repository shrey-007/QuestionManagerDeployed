package com.smart.controller;

import com.smart.entities.Question;
import com.smart.entities.QuestionExplaination;
import com.smart.entities.User;
import com.smart.model.QuestionExplainationRepository;
import com.smart.model.QuestionRepository;
import com.smart.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionExplainationRepository questionExplainationRepository;

    @RequestMapping("/editNote")
    public String editNote(@RequestParam("note") String note, HttpSession session, Model model,@RequestParam("qqid") String qqid){
        //get the user
        User user=(User) model.getAttribute("user");

        int qid=Integer.parseInt(qqid);

        //get the question
        Optional<Question> questionOptional=this.questionRepository.findById(qid);
        Question question=questionOptional.get();

        System.out.println("============================================================================================================================================================================================"+qid+"======="+note);

        //change the note in question
        QuestionExplaination questionExplaination=questionExplainationRepository.findByQid(qid);
        questionExplaination.setExplaination(note);
        questionExplainationRepository.save(questionExplaination);


        //return
        model.addAttribute("question",question);
        model.addAttribute("questionExplaination",questionExplaination);
        return "showContactDetail";
    }
}
