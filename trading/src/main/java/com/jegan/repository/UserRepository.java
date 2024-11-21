package com.jegan.repository;

import com.jegan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long>{


    User findByEmail(String email);
}
