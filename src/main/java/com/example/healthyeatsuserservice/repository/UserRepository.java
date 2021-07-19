package com.example.healthyeatsuserservice.repository;

import com.example.healthyeatsuserservice.models.Role;
import com.example.healthyeatsuserservice.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByCompanyName(String companyName);
    List<User> findUsersByRole(Role role);
    Optional<User> findUserByEmail(String email);
}
