package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.UserDto;
import com.kamiljach.devjobshub.exceptions.exceptions.InvalidRequestException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.request.user.UpdateUserRequest;
import com.kamiljach.devjobshub.service.UserService;
import jakarta.validation.Valid;
import org.hibernate.sql.Update;
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

    @PutMapping("user")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest, @RequestHeader("Authorization") String jwt) throws InvalidRequestException, UserNotFoundByIdException, UserNotFoundByJwtException {
        userService.updateUser(updateUserRequest, jwt);
        return new ResponseEntity<>(User.mapUserToUserDtoShallow(userService.findUserByJwt(jwt)), HttpStatus.OK);
    }
}
