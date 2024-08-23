package com.smart.model;

import com.smart.entities.Question;
import com.smart.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {

    //we will not return List<Question>, instead we will return Page<Question>
    //A Page is a sublist of a list of objects
    //Pageable is an abstract interface for pagination information ki kis page pr currently hai and ek page mai kitne questions display hoge
    @Query(value = "select * from question where user_id=:userId",nativeQuery = true)
    public Page<Question> findQuestionsByUser(@Param("userId") int userId, Pageable pageable);

    //ye method search-bar  ke liye hai.vaha se user and and keyword aaega and user ke saare questions jisme vo keyword ho, unki list
    //fetch hokr aaegi.we dont need to provide implementation of this method.Spring boot will itself do it .
    public List<Question> findByNameContainingAndUser(String keyword,User user);

}
