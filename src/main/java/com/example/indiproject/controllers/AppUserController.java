package com.example.indiproject.controllers;

import com.example.indiproject.DTOs.*;
import com.example.indiproject.repositories.AppUserRepository;
import com.example.indiproject.services.api.AppUserService;
import com.example.indiproject.services.api.ItemService;
import com.example.indiproject.services.util.BearerTokenWrapper;
import com.example.indiproject.services.util.JwtUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AppUserController {
    private final JwtUtility jwtUtility;
    private final BearerTokenWrapper tokenWrapper;
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final ItemService itemService;
    private final AppUserRepository appUserRepository;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity AppUserRegistration(@RequestBody RegistrationDTO registrationDTO) {
        if (registrationDTO.getUsername().equals("")) {
            log.error("Username is empty");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Username is empty"));
        } else if (registrationDTO.getPassword().equals("")) {
            log.error("Password is empty");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Password is empty"));
        } else if (appUserService.appUserWithUserNameExists(registrationDTO.getUsername())) {
            log.error("UserName is already taken");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("UserName is already taken"));
        } else if (!appUserService.appUserNameIsAppropriate(registrationDTO.getUsername())) {
            return ResponseEntity.status(400).body(new ErrorMessageDTO("UserName must be appropriate"));
        } else {
            appUserService.saveAppUser(registrationDTO);
            log.info("User " + registrationDTO.getUsername() + " registered successfully");
            return ResponseEntity.status(200).body(new StatusDTO("ok"));
        }
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        if (loginDTO.getUsername().equals("")) {
            log.error("Username is empty");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Username is empty"));
        } else if (loginDTO.getPassword().equals("")) {
            log.error("Password is empty");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Password is empty"));
        } else if (!appUserService.appUserWithUserNameExists(loginDTO.getUsername())) {
            log.error("The user " + loginDTO.getUsername() + " does not exist.");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("The user " + loginDTO.getUsername() + " does not exist."));
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(), loginDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.error("Username and/or password was incorrect!");
            return ResponseEntity.status(401).body(new ErrorMessageDTO("Username and/or password was incorrect!"));
        }
        UserDetails userDetails = appUserService.loadUserByUsername(loginDTO.getUsername());
        String token = jwtUtility.generateToken(userDetails);
        log.info("User " + loginDTO.getUsername() + " logged successfully");
        return ResponseEntity.ok(new LoginResponseDTO(HttpStatus.OK, token));
    }

    @PutMapping("/user/{id}/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity createItem(@RequestBody CreateItemDTO createItemDTO,
                                     @PathVariable Long id) {
        if (!appUserService.getIdByUsername(jwtUtility.getUsernameFromToken(tokenWrapper.getToken())).equals(id)) {
            log.error("The user is not authenticated");
            return ResponseEntity.status(401).body(new ErrorMessageDTO("The user is not authenticated"));
        } else if (createItemDTO.getName().equals("")) {
            log.error("Name is empty");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Name is empty"));
        } else if (createItemDTO.getDescription().equals("")) {
            log.error("Description is empty");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Description is empty"));
        } else if (!itemService.urlIsValid(createItemDTO.getPhotoUrl())) {
            log.error("Photo URL is not valid");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Photo URL is not valid"));
        } else if (createItemDTO.getPrice() <= 0) {
            log.error("The price is not positive number");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("The price is not positive number"));
        } else {
            itemService.createItem(createItemDTO, id);
            log.info("Item was created successfully");
            return ResponseEntity.status(200).body(new StatusDTO("ok"));
        }
    }

    @GetMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity listItems(@RequestParam(required = false) Integer n) {
        return ResponseEntity.status(200).body(itemService.listItems(n));
    }

    @GetMapping("/item/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity viewItem(@PathVariable Long id) {
        return ResponseEntity.status(200).body(itemService.viewItem(id));
    }

    @PutMapping("/{uid}/bid/{iid}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity bid(@PathVariable("uid") Long appUserId,
                              @PathVariable("iid") Long itemId,
                              @RequestBody BidDTO bidDTO) {
        if (!itemService.itemWithIdExists(itemId)) {
            log.error("The item doesn't exist");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("The item doesn't exist"));
        } else if (!itemService.hasEnoughDollars(appUserId, bidDTO.getBid())) {
            log.error("Not enough money on the account");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Not enough money on the account"));
        } else if (!itemService.bidHighEnough(itemId, bidDTO.getBid())) {
            log.error("Bid not high enough");
            return ResponseEntity.status(400).body(new ErrorMessageDTO("Bid not high enough"));
        } else {
            itemService.bid(appUserId, itemId, bidDTO.getBid());
            log.info("Bid was set successfully");
            return ResponseEntity.status(200).body(new StatusDTO("ok"));
        }
    }

}
