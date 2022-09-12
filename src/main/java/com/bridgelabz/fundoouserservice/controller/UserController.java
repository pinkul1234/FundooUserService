package com.bridgelabz.fundoouserservice.controller;

import com.bridgelabz.fundoouserservice.dto.UserDto;
import com.bridgelabz.fundoouserservice.model.UserModel;
import com.bridgelabz.fundoouserservice.service.IUserService;
import com.bridgelabz.fundoouserservice.util.Response;
import com.bridgelabz.fundoouserservice.util.ResponseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping("/adduser")
    public ResponseEntity<Response> addUser(@RequestBody UserDto userDto){
        Response response = userService.addUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@RequestHeader String token, @RequestBody UserDto userDto, @PathVariable long userId){
        Response response = userService.updateUser(userId, token, userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getuserdata")
    public ResponseEntity<List<?>> getUserData(@RequestParam String token){
        List<UserModel> response = userService.getUserData(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/deleteuser")
    public ResponseEntity<Response> deleteUser(@PathVariable long userId, @RequestHeader String token){
        Response response = userService.deleteUser(userId, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseToken> login(@RequestParam String email, @RequestParam String password){
        ResponseToken responseToken = userService.login(email, password);
        return new ResponseEntity<>(responseToken, HttpStatus.OK);
    }
    @PutMapping("/changepassword")
    public ResponseEntity<Response> changePassword(@RequestHeader String token, @RequestParam String password){
        Response response = userService.changePassword(token, password);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/resetpassword")
    public ResponseEntity<Response> resetPassword(@RequestParam String emailId){
        Response response = userService.resetPassword(emailId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/deleteusers")
    public ResponseEntity<Response> deleteUsers(@PathVariable long userId, @RequestHeader String token) {
        Response response = userService.deleteUsers(userId, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/deletepermanently")
    public ResponseEntity<Response> deletePermanently(@PathVariable long userId, @RequestHeader String token) {
        Response response = userService.deletePermanently(userId, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/restore")
    public ResponseEntity<Response> restore(@PathVariable long userId, @RequestHeader String token) {
        Response response = userService.restore(userId, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/validate/{token}")
    public Boolean validate(@PathVariable String token){
        return userService.validate(token);
    }
}
