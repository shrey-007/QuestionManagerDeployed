package com.smart.controller;

import com.smart.entities.Question;
import com.smart.entities.User;
import com.smart.model.QuestionRepository;
import com.smart.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//this will not return any page but will return string as a json object
@RestController
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, HttpSession session, Model model){
        //fetch user
        User user=(User)model.getAttribute("user");

        System.out.println(query);

        //fetch list of questions to be displayed on search bar, according to query
        List<Question> questionList=this.questionRepository.findByNameContainingAndUser(query,user);

        //yaha se serealize hokr questions pahuch jaaege
        return ResponseEntity.ok(questionList);
    }
}
