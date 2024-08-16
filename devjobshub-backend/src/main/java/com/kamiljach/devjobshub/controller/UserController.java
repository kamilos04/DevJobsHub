package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestParam String token, @RequestHeader("Authorization") String jwt) throws UserNotFoundByJwtException {
        return new ResponseEntity<>(userService.findUserByJwt(token), HttpStatus.OK);
    }
}
