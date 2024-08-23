package com.smart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int qid;
    private String name;
    private String description;


    private String fileName; //fileName is name of the containing the code
    private String imageName;
    @ManyToOne
    @JsonIgnore
    //to avoid circular dependency we have used json ignore.
    private User user;

    @Override
    public String toString() {
        return "Question{" +
                "qid=" + qid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", fileName='" + fileName + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
