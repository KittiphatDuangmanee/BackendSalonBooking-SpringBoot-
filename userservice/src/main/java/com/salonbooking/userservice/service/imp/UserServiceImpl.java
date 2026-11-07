package com.salonbooking.userservice.service.imp;

import com.salonbooking.userservice.exception.UserException;
import com.salonbooking.userservice.modal.User;
import com.salonbooking.userservice.payload.dto.KeyCloakUserDTO;
import com.salonbooking.userservice.repository.UserRepository;
import com.salonbooking.userservice.service.KeycloakService;
import com.salonbooking.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws UserException {
        Optional<User> otp = userRepository.findById(id);
        if(otp.isPresent()){
            return otp.get();
        }
        throw  new UserException("User not found");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) throws UserException {
        Optional<User> otp = userRepository.findById(id);
        if(otp.isEmpty()){
            throw new UserException("user not found with id" + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(Long id, User user) throws UserException {
        Optional<User> otp = userRepository.findById(id);
        if(otp.isEmpty()){
            throw new UserException("user not found with id" + id);
        }
        User existUser = otp.get();
        existUser.setFullName(user.getFullName());
        existUser.setEmail(user.getEmail());
        existUser.setRole(user.getRole());
        existUser.setPhone(user.getPhone());
        existUser.setUsername(user.getUsername());
        existUser.setPassword(user.getPassword());
        return userRepository.save(existUser);
    }

    @Override
    public User getUserFromJwt(String jwt) throws Exception {
        KeyCloakUserDTO keyCloakUserDTO = keycloakService.fetchUserProfileByJwt(jwt);
        return userRepository.findByEmail(keyCloakUserDTO.getEmail());
    }
}
