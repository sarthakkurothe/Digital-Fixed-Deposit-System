package com.fdsystem.backend.service;


import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Service class that provides operations related to user management.
 * Handles operations such as adding users, checking if users exist, and retrieving user information.
 */
@Slf4j
@Service
public class UserService {
    private UserRepository userRepository;
    
    /**
     * Constructor for UserService.
     * 
     * @param userRepository The repository for user data access operations
     */
    public UserService(UserRepository userRepository){
      this.userRepository = userRepository;
    }
    /**
     * Adds a new user to the system.
     * 
     * @param user The User entity to be added
     * @return true if user was added successfully, false if there was an error
     */
    public boolean addUser(User user){
      try {
        userRepository.save(user);
        return true;
      } catch (Exception e) {
        log.error("Error while adding User {}",e.getMessage());
      }
      return false;
    }
    /**
     * Checks if a user with the given email exists in the system.
     * 
     * @param email The email to check
     * @return true if a user with the email exists, false otherwise
     * @throws IllegalArgumentException if email is null
     */
    public boolean isUserExists(String email){
      if(email == null) {
        throw new IllegalArgumentException("Email must not be null");
      }
      return userRepository.findByEmail(email) != null;
    }

    /**
     * Retrieves a user by their ID.
     * 
     * @param userId The ID of the user to retrieve
     * @return An Optional containing the User if found, or empty if not found
     */
    public Optional<User> getUserById(long userId) {
      return this.userRepository.findById(userId);
    }
    /**
     * Gets the total count of all users in the system.
     * 
     * @return The total number of users
     */
    public Long getAllUsersCount(){
      return userRepository.findAllUserCount();
    }

}
