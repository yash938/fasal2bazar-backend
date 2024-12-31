package com.agriRoot.controller;

import com.agriRoot.dto.JwtRequest;
import com.agriRoot.dto.JwtResponse;
import com.agriRoot.dto.UserDto;
import com.agriRoot.security.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        log.info("Username {} and password {}", jwtRequest.getEmail(), jwtRequest.getPassword());
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());

        String token = jwtHelper.generateToken(userDetails);
        JwtResponse user = JwtResponse.builder().token(token).user(modelMapper.map(userDetails, UserDto.class)).build();
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    private void doAuthenticate(String email, String password) {
        try {
            Authentication authentication= new UsernamePasswordAuthenticationToken(email, password);

            log.info("User authenticated successfully: {}", email);
        } catch (BadCredentialsException ex) {
            log.error("Invalid credentials for email {}: {}", email, ex.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}