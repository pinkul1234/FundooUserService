package com.bridgelabz.fundoouserservice.service;

import com.bridgelabz.fundoouserservice.dto.UserDto;
import com.bridgelabz.fundoouserservice.model.UserModel;
import com.bridgelabz.fundoouserservice.util.Response;
import com.bridgelabz.fundoouserservice.util.ResponseClass;
import com.bridgelabz.fundoouserservice.util.ResponseToken;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    Response addUser(UserDto userDto);


    Response updateUser(Long userId, String token, UserDto userDto);

    List<UserModel> getUserData(String token);

    ResponseToken login(String email, String password);

    Response changePassword(String token, String password);

    Response resetPassword(String emailId);

    Response deleteUser(Long userId, String token);

    Response deleteUsers(Long userId, String token);

    Response deletePermanently(Long userId, String token);

    Response restore(Long userId, String token);

    Boolean validate(String token);

    Response addProfilePic(Long id, MultipartFile profilePic) throws IOException;

    ResponseClass validateEmail(String emailId, String token);
}
