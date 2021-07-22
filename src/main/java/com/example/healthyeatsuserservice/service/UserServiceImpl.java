package com.example.healthyeatsuserservice.service;

import com.example.healthyeatsuserservice.controllers.requests.OrganizationRegistrationRequest;
import com.example.healthyeatsuserservice.exceptions.MealPlanException;
import com.example.healthyeatsuserservice.exceptions.UserException;
import com.example.healthyeatsuserservice.models.MealPlan;
import com.example.healthyeatsuserservice.models.Preference;
import com.example.healthyeatsuserservice.models.Role;
import com.example.healthyeatsuserservice.models.User;
import com.example.healthyeatsuserservice.repository.UserRepository;
import com.example.healthyeatsuserservice.controllers.requests.IndividualRegistrationRequest;
import com.example.healthyeatsuserservice.service.dtos.UserDTO;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public UserDTO updateUserDetails(String userId, UserDTO userDTO) {
        return null;
    }

    @Override
    public void addPreference(String userId, List<String> preferences) throws UserException {
        User user = findUserById(userId);
        for (String userPreference : preferences) {
            for (Preference preference : Preference.values()) {
                if (userPreference.equalsIgnoreCase(preference.name())) {
                    user.getPreferences().add(preference);
                }
            }
        }
        userRepository.save(user);
    }

    @Override
    public void removePreference(String userId, List<String> preferences) throws UserException {
        User user = findUserById(userId);
        for (String userPreference : preferences) {
            for (Preference preference : Preference.values()) {
                if (userPreference.equalsIgnoreCase(preference.name())) {
                    user.getPreferences().remove(preference);
                }
            }
        }
        userRepository.save(user);
    }

    @Override
    public MealPlan createCustomMealPlan() {
        return null;
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

    private User findUserById(String id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new UserException("No user found with Id " + id)
                );
    }

    private boolean usernameExists(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    private boolean emailExist(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }


}
