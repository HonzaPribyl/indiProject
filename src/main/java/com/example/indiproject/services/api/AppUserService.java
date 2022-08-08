package com.example.indiproject.services.api;

import com.example.indiproject.DTOs.LoginDTO;
import com.example.indiproject.DTOs.RegistrationDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AppUserService {
    boolean appUserWithUserNameExists(String userName);

    boolean appUserNameIsAppropriate(String userName);

    boolean passwordMatchesAppUserName(LoginDTO loginDTO);

    void saveAppUser(RegistrationDTO registrationDTO);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Long getIdByUsername(String username);
}
