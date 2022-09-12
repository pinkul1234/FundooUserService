package com.bridgelabz.fundoouserservice.service;

import com.bridgelabz.fundoouserservice.dto.UserDto;
import com.bridgelabz.fundoouserservice.model.UserModel;
import com.bridgelabz.fundoouserservice.util.Response;
import com.bridgelabz.fundoouserservice.util.ResponseToken;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    Response addUser(UserDto userDto);


    Response updateUser(long userId, String token, UserDto userDto);

    List<UserModel> getUserData(String token);

    ResponseToken login(String email, String password);

    Response changePassword(String token, String password);

    Response resetPassword(String emailId);

    Response deleteUser(long userId, String token);

    Response deleteUsers(long userId, String token);

    Response deletePermanently(long userId, String token);

    Response restore(long userId, String token);

    Boolean validate(String token);
}
