package com.sales.din.controller;

import com.sales.din.dto.LoginDTO;
import com.sales.din.dto.ResponseDTO;
import com.sales.din.dto.TokenDTO;
import com.sales.din.security.CustomerUserDetailService;
import com.sales.din.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private CustomerUserDetailService userDetailService;

    @Autowired
    private JwtService jwtService;

    @Value("${security.jwt.expiration}")
    private String expiration;

    @PostMapping()
    public ResponseEntity post(@Valid @RequestBody LoginDTO loginData) {
        try {
            userDetailService.verifyUserCredentials(loginData);
            String token = jwtService.generateToken(loginData.getUsername());
            return new ResponseEntity(new TokenDTO(token, expiration), HttpStatus.OK);
        } catch (Exception error){
            return new ResponseEntity(new ResponseDTO<>(error.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

}
