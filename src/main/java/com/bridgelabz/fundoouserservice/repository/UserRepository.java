package com.bridgelabz.fundoouserservice.repository;

import com.bridgelabz.fundoouserservice.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmailId(String email);

}
