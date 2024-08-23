package com.smart.model;

import com.smart.entities.QuestionExplaination;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionExplainationRepository extends MongoRepository<QuestionExplaination,Integer> {
    public QuestionExplaination findByQid(int qid);
}
