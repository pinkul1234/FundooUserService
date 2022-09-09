package com.bridgelabz.fundoouserservice.service;

import com.bridgelabz.fundoouserservice.dto.UserDto;
import com.bridgelabz.fundoouserservice.model.UserModel;
import com.bridgelabz.fundoouserservice.util.Response;
import com.bridgelabz.fundoouserservice.util.ResponseToken;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    Response addUser(UserDto userDto);


    Response updateUser(long id, String token, UserDto userDto);

    List<UserModel> getUserData(String token);

    ResponseToken login(String email, String password);

    Response changePassword(String token, String password);

    Response resetPassword(String emailId);

    Response deleteUser(long id, String token);
}
