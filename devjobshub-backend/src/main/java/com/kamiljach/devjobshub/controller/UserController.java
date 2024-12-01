package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.UserDto;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
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

    @GetMapping("/myprofile")
    public ResponseEntity<UserDto> getMyProfile(@RequestHeader("Authorization") String jwt) throws UserNotFoundByJwtException {
        return new ResponseEntity<>(User.mapUserToUserDtoShallow(userService.findUserByJwt(jwt)), HttpStatus.OK);
    }
}
