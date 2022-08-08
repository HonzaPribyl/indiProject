package com.example.indiproject.services.impl;

import com.example.indiproject.DTOs.LoginDTO;
import com.example.indiproject.DTOs.RegistrationDTO;
import com.example.indiproject.models.AppUser;
import com.example.indiproject.repositories.AppUserRepository;
import com.example.indiproject.services.api.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepository appUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean appUserWithUserNameExists(String userName) {
        return (appUserRepository.findByUsername(userName) != null);
    }

    @Override
    public boolean appUserNameIsAppropriate(String userName) {
        userName = userName.toLowerCase();
        if (userName.contains("fuck")) {
            return false;
        } else if (userName.contains("shit")) {
            return false;
        } else if (userName.contains("bitch")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean passwordMatchesAppUserName(LoginDTO loginDTO) {
        AppUser appUser = appUserRepository.findByUsername(loginDTO.getUsername());
        return appUser.getPassword().equals(loginDTO.getPassword());
    }

    @Override
    public void saveAppUser(RegistrationDTO registrationDTO) {
        appUserRepository.save(new AppUser(registrationDTO.getUsername(), passwordEncoder.encode(registrationDTO.getPassword()), registrationDTO.getDollars()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);

        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(appUser.getUsername(), appUser.getPassword(), new ArrayList<>());
    }

    @Override
    public Long getIdByUsername(String username) {
        return appUserRepository.findByUsername(username).getId();
    }
}
