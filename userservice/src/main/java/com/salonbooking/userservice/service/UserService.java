package com.salonbooking.userservice.service;

import com.salonbooking.userservice.exception.UserException;
import com.salonbooking.userservice.modal.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id) throws UserException;
    List<User> getAllUsers();
    void deleteUser(Long id) throws UserException;
    User updateUser(Long id, User user) throws UserException;
    User getUserFromJwt(String jwt) throws Exception;
}
