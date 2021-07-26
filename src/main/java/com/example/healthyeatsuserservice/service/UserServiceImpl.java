package com.example.healthyeatsuserservice.service;

import com.example.healthyeatsuserservice.controllers.requests.*;
import com.example.healthyeatsuserservice.exceptions.MealPlanException;
import com.example.healthyeatsuserservice.exceptions.TokenException;
import com.example.healthyeatsuserservice.exceptions.UserException;
import com.example.healthyeatsuserservice.models.*;
import com.example.healthyeatsuserservice.repository.TokenRepository;
import com.example.healthyeatsuserservice.repository.UserRepository;
import com.example.healthyeatsuserservice.service.dtos.MealPlanDTO;
import com.example.healthyeatsuserservice.service.dtos.UserDTO;
import com.example.healthyeatsuserservice.service.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "userService")
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadAUserByUsername(username);
    }

    private UserDetails loadAUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("No user found with this username %s", username))
                );
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(
                role -> {
                    authorities.addAll(role.getGrantedAuthorities());
                }
        );
        return authorities;
    }

    @Override
    public UserDTO registerIndividual(IndividualRegistrationRequest details) throws UserException {
        if (usernameExists(details.getUsername())) {
            throw new UserException(String.format("username already exits %s", details.getUsername()));
        }
        if (emailExist(details.getEmail())) {
            throw new UserException(String.format("email already exists %s", details.getEmail()));
        }
        User newUser = modelMapper.map(details, User.class);
        newUser.getRoles().add(Role.INDIVIDUAL);
        newUser.setPassword(bcryptPasswordEncoder.encode(details.getPassword()));
        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserDTO.class);

    }

    @Override
    public UserDTO registerOrganization(OrganizationRegistrationRequest details) throws UserException {
        if (emailExist(details.getEmail())) {
            throw new UserException(String.format("email already exists %s", details.getEmail()));
        }
        User newOrganization = modelMapper.map(details, User.class);
        newOrganization.setUsername(details.getEmail());
        newOrganization.getRoles().add(Role.ORGANIZATION);
        newOrganization.setPassword(bcryptPasswordEncoder.encode(details.getPassword()));
        User savedOrganization = userRepository.save(newOrganization);
        return modelMapper.map(savedOrganization, UserDTO.class);
    }

    @Override
    public UserDTO registerAdmin(IndividualRegistrationRequest details) throws UserException {
        if (usernameExists(details.getUsername())) {
            throw new UserException(String.format("username already exits %s", details.getUsername()));
        }
        if (emailExist(details.getEmail())) {
            throw new UserException(String.format("email already exists %s", details.getEmail()));
        }
        User newUser = modelMapper.map(details, User.class);
        newUser.getRoles().add(Role.ADMIN);
        newUser.setPassword(bcryptPasswordEncoder.encode(details.getPassword()));
        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO findUserByUserName(String username) {
        User user = userRepository.findUserByUsername(username).orElse(null);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUserDetails(String userId, UserDTO userDTO) throws UserException {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(String.format("No user found with id: %s", userId)));
        modelMapper.map(userDTO, userToUpdate);
        User updatedUser = userRepository.save(userToUpdate);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    //Todo add logic to prevent duplicate prefs
    public void addPreference(String userId, PreferenceRequest request) throws UserException {
        User user = findUserById(userId);
        for (String userPreference : request.getPreferences()) {
            for (Preference preference : Preference.values()) {
                if (userPreference.equalsIgnoreCase(preference.name())) {
                    user.getPreferences().add(preference);
                }
            }
        }
        userRepository.save(user);
    }

    @Override
    public void removePreference(String userId, PreferenceRequest request) throws UserException {
        User user = findUserById(userId);
        for (String userPreference : request.getPreferences()) {
            for (Preference preference : Preference.values()) {
                if (userPreference.equalsIgnoreCase(preference.name())) {
                    user.getPreferences().remove(preference);
                }
            }
        }
        userRepository.save(user);
    }

    @Override
    public MealPlan createCustomMealPlan(String userId, MealPlanDTO mealPlanDTO) throws UserException, MealPlanException {
        User user = findUserById(userId);
        ResponseEntity<?> response = mealPlanService.createCustomMealPlan(mealPlanDTO);
        if (response.getStatusCode().is5xxServerError()) {
            throw new MealPlanException("Looks like something went wrong, please try again");
        }
        MealPlan plan = new MealPlan();
        modelMapper.map(response.getBody(), plan);
        user.getPlans().add(plan);
        userRepository.save(user);
        return plan;
    }

    @Override
    public void cancelCustomMealPlan(String userId, String planId) throws UserException, MealPlanException {
        User user = findUserById(userId);
        ResponseEntity<?> response = mealPlanService.deleteMealPlan(planId);
        if (response.getStatusCode().is4xxClientError()) {
            throw new MealPlanException("No meal Plan found with that Id");
        }
        if (response.getStatusCode().is5xxServerError()) {
            throw new MealPlanException("Looks like something went wrong, please try again");
        }
        List<MealPlan> updatedPlans = user.getPlans().stream().filter(mealPlan -> !mealPlan.getId().equals(planId)).collect(Collectors.toList());
        user.setPlans(updatedPlans);
        userRepository.save(user);
    }

    @Override
    public List<MealPlan> getUserMealPlans(String userId) throws UserException {
        User user = findUserById(userId);
        return user.getPlans();
    }

    @Override
    public void subscribeUserToMealPlan(String userId, String planId) throws UserException, MealPlanException {
        User user = findUserById(userId);
        ResponseEntity<?> response = mealPlanService.addUserToMealPlan(planId, user);
        log.info("body ---> {}", response.getBody());
        if (response.getStatusCode().is4xxClientError()) {
            throw new MealPlanException("No mealPlan found with that Id");
        }
        if (response.getStatusCode().is5xxServerError()) {
            throw new MealPlanException("Looks like something went wrong, please try again");
        }
        MealPlan plan = new MealPlan();
        modelMapper.map(response.getBody(), plan);
        user.getPlans().add(plan);
        userRepository.save(user);
    }

    @Override
    public void unsubscribeUserFromMealPlan(String userId, String planId) throws UserException, MealPlanException {
        User user = findUserById(userId);
        ResponseEntity<?> response = mealPlanService.removeUserFromMealPlan(planId, user);
        log.info("body ---> {}", response.getBody());
        if (response.getStatusCode().is4xxClientError()) {
            throw new MealPlanException("No mealPlan found with that Id");
        }
        if (response.getStatusCode().is5xxServerError()) {
            throw new MealPlanException("Looks like something went wrong, please try again");
        }
        MealPlan plan = new MealPlan();
        modelMapper.map(response.getBody(), plan);
        user.getPlans().remove(plan);
        userRepository.save(user);

    }

    @Override
    public Token generatePasswordResetToken(String username) throws UserException {
        User userToResetPassword = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserException("No user found with user name " + username));
        Token token = new Token();
        token.setType(TokenType.PASSWORD_RESET);
        token.setUser(userToResetPassword);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiry(LocalDateTime.now().plusMinutes(30));
        return tokenRepository.save(token);
    }

    @Override
    public void resetUserPassword(PasswordResetRequest request, String passwordResetToken) throws UserException, TokenException {
        String username = request.getUsername();
        String newPassword = request.getNewPassword();
        User userToResetPassword = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserException("No user found with user name " + username));
        Token token = tokenRepository.findByToken(passwordResetToken)
                .orElseThrow(() -> new TokenException(String.format("No token with value %s found", passwordResetToken)));
        if (token.getExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenException("This password reset token has expired ");
        }
        if (!token.getUser().getId().equals(userToResetPassword.getId())) {
            throw new TokenException("This password rest token does not belong to this user");
        }
        userToResetPassword.setPassword(bcryptPasswordEncoder.encode(newPassword));
        userRepository.save(userToResetPassword);
        tokenRepository.delete(token);
    }

    @Override
    public void updateUserPassword(PasswordChangeRequest request) throws UserException {
        String username = request.getUsername();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();
        User userToChangePassword = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserException("No user found with user name " + username));

        boolean passwordMatch = bcryptPasswordEncoder.matches(oldPassword, userToChangePassword.getPassword());
        if (!passwordMatch) {
            throw new UserException("Passwords do not match");
        }
        userToChangePassword.setPassword(bcryptPasswordEncoder.encode(newPassword));
        userRepository.save(userToChangePassword);
    }

    @Override
    public User internalFindUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    private User findUserById(String id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new UserException("No user found with Id " + id)
                );
    }

    @Override
    public User internalFindUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);

    }

    @Override
    public User internalFindUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }

    private boolean usernameExists(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    private boolean emailExist(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }


}
