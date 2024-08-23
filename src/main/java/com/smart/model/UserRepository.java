package com.smart.model;

import com.smart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    //since user ki entity class mai email ko unique attribute de diya hai toh ye single user nikaal kr dega.
    //agar koi user vaapis se same email se register krega toh spring boot validation se error aaega
    public User findByEmail(String email);

}
