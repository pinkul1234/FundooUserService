package com.bridgelabz.fundoouserservice.service;

import com.bridgelabz.fundoouserservice.dto.UserDto;
import com.bridgelabz.fundoouserservice.exception.UserNotFoundException;
import com.bridgelabz.fundoouserservice.model.UserModel;
import com.bridgelabz.fundoouserservice.repository.UserRepository;
import com.bridgelabz.fundoouserservice.util.Response;
import com.bridgelabz.fundoouserservice.util.ResponseToken;
import com.bridgelabz.fundoouserservice.util.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    MailService mailService;

    @Override
    public Response addUser(UserDto userDto) {
        UserModel userModel = new UserModel(userDto);
        userRepository.save(userModel);
        String body = "User added: " + userModel.getId();
        String subject = "User registration successfully";
        mailService.send(userModel.getEmailId(), body, subject);
        return new Response("Success", 200, userModel);
    }

    @Override
    public Response updateUser(long id, String token, UserDto userDto) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userId);
        if (isUserPresent.isPresent()) {
            isUserPresent.get().setFullName(userDto.getFullName());
            isUserPresent.get().setMobile(userDto.getMobile());
            isUserPresent.get().setEmailId(userDto.getEmailId());
            isUserPresent.get().setProfilePic(userDto.getProfilePic());
            isUserPresent.get().setPassword(userDto.getPassword());
            isUserPresent.get().setDateOfBirth(userDto.getDateOfBirth());
            userRepository.save(isUserPresent.get());
            return new Response("Success", 200, isUserPresent.get());
        }
        throw new UserNotFoundException(400, "User Not Present!!!");
    }

    @Override
    public List<UserModel> getUserData(String token) {
        List<UserModel> getalluserdata = userRepository.findAll();
        if (getalluserdata.size() > 0) {
            return getalluserdata;
        }
        throw new UserNotFoundException(400, "User Not Found");
    }

    @Override
    public Response deleteUser(long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userId);
        if (isUserPresent.isPresent()) {
            userRepository.save(isUserPresent.get());
            String body = "User deleted: " + isUserPresent.get().getId();
            String subject = "User deleted successfully";
            mailService.send(isUserPresent.get().getEmailId(), body, subject);
            return new Response("Success", 200, isUserPresent.get());
        }
        throw new UserNotFoundException(400, "User does not found");
    }

    @Override
    public ResponseToken login(String email, String password) {
        Optional<UserModel> isUserPresent = userRepository.findByEmailId(email);
        if (isUserPresent.isPresent()) {
            if (isUserPresent.get().getPassword().equals(password)) {
                String token = tokenUtil.createToken(isUserPresent.get().getId());
                return new ResponseToken(200, "success", token);
            }
            throw new UserNotFoundException(400, "Invalid Credentials");
        }
        throw new UserNotFoundException(400, "User Not Found");
    }

    @Override
    public Response changePassword(String token, String password) {
        Long id = tokenUtil.decodeToken(token);
        Optional<UserModel> isIdPresent = userRepository.findById(id);
        if (isIdPresent.isPresent()) {
            isIdPresent.get().setPassword(password);
            userRepository.save(isIdPresent.get());
            return new Response("Success", 200, isIdPresent.get());
        } else {
            throw new UserNotFoundException(400, "Password is worng");
        }
    }

    @Override
    public Response resetPassword(String emailId) {
        Optional<UserModel> isEmailPresent = userRepository.findByEmailId(emailId);
        if (isEmailPresent.isPresent()) {
            String token = tokenUtil.createToken(isEmailPresent.get().getId());
            String url = "http://localhost:8087/user/changePassword";
            String subject = "reset password";
            String body = "For reset password click this link" + url + "use this to reset" + token;
            mailService.send(isEmailPresent.get().getEmailId(), body, subject);
        }
        return new Response("Success", 200, isEmailPresent.get());
    }

    @Override
    public Response deleteUsers(long id, String token) {
        long userId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUser = userRepository.findById(userId);
        if (isUser.isPresent()) {
            Optional<UserModel> isId = userRepository.findById(id);
            if (isId.isPresent()) {
                isId.get().setActive(false);
                isId.get().setDeleted(true);
                userRepository.save(isId.get());
                return new Response("success", 200, isId.get());
            }
            throw new UserNotFoundException(400,"Not found");
        }
        throw new UserNotFoundException(400, "Token is wrong");
    }
    @Override
    public Response deletePermanently(long id, String token){
        long UserId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUser = userRepository.findById(UserId);
        if (isUser.isPresent()){
            Optional<UserModel> isId = userRepository.findById(id);
            if (isId.isPresent()){
                return new Response("success", 200, isId.get());
            }
            throw new UserNotFoundException(400, "Not found");
        }
        throw new UserNotFoundException(400, "Token is wrong");
    }
    @Override
    public Response restore(long id, String token){
        long userId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUser = userRepository.findById(userId);
        if (isUser.isPresent()){
            Optional<UserModel> isId = userRepository.findById(id);
            if (isId.isPresent()){
                isId.get().setActive(true);
                isId.get().setDeleted(false);
                userRepository.save(isId.get());
                return new Response("success", 200, isId.get());
            }
            throw new UserNotFoundException(400, "Not found");
        }
        throw new UserNotFoundException(400, "Token is wrong");
    }
}
