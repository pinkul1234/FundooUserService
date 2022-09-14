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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
    public Response updateUser(long userId, String token, UserDto userDto) {
        Long userIdToken = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userIdToken);
        if (isUserPresent.isPresent()) {
            isUserPresent.get().setFullName(userDto.getFullName());
            isUserPresent.get().setMobile(userDto.getMobile());
            isUserPresent.get().setEmailId(userDto.getEmailId());
            isUserPresent.get().setPassword(userDto.getPassword());
            isUserPresent.get().setDateOfBirth(userDto.getDateOfBirth());
            isUserPresent.get().setUpdatedDate(LocalDateTime.now());
            String body = "Users details updated with id is: " +isUserPresent.get().getId();
            String subject = "Users details updated successfully";
            mailService.send(isUserPresent.get().getEmailId(), body, subject);
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
    public Response deleteUser(long userId, String token) {
        Long userIdToken = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userIdToken);
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
    public Response deleteUsers(long userId, String token) {
        long userIdToken = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userIdToken);
        if (isUserPresent.isPresent()) {
            Optional<UserModel> isIdPresent = userRepository.findById(userId);
            if (isIdPresent.isPresent()) {
                isIdPresent.get().setActive(false);
                isIdPresent.get().setDeleted(true);
                userRepository.save(isIdPresent.get());
                return new Response("success", 200, isIdPresent.get());
            }
            throw new UserNotFoundException(400,"Not found");
        }
        throw new UserNotFoundException(400, "Token is wrong");
    }
    @Override
    public Response deletePermanently(long userId, String token){
        long userIdToken = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userIdToken);
        if (isUserPresent.isPresent()){
            Optional<UserModel> isIdPresent = userRepository.findById(userId);
            if (isIdPresent.isPresent()){
                userRepository.delete(isIdPresent.get());
                return new Response("success", 200, isIdPresent.get());
            }
            throw new UserNotFoundException(400, "Not found");
        }
        throw new UserNotFoundException(400, "Token is wrong");
    }
    @Override
    public Response restore(long userId, String token){
        long userIdToken = tokenUtil.decodeToken(token);
        Optional<UserModel> isUserPresent = userRepository.findById(userIdToken);
        if (isUserPresent.isPresent()){
            Optional<UserModel> isIdPresent = userRepository.findById(userId);
            if (isIdPresent.isPresent()){
                isIdPresent.get().setActive(true);
                isIdPresent.get().setDeleted(false);
                userRepository.save(isIdPresent.get());
                return new Response("success", 200, isIdPresent.get());
            }
            throw new UserNotFoundException(400, "Not found");
        }
        throw new UserNotFoundException(400, "Token is wrong");
    }

    @Override
    public Response addProfilePic(long id, MultipartFile profilePic) throws IOException {
        Optional<UserModel> isIdPresent = userRepository.findById(id);
        if (isIdPresent.isPresent()){
            isIdPresent.get().setProfilePic(profilePic.getBytes());
            return new Response("success", 200, isIdPresent.get());
        }
        return null;
    }

    @Override
    public Boolean validate(String token){
        Long userId = tokenUtil.decodeToken(token);
        Optional<UserModel> isUser = userRepository.findById(userId);
        if (isUser.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public Boolean validateEmail(String emailId){
        Optional<UserModel> isEmailPresent = userRepository.findByEmailId(emailId);
        if (isEmailPresent.isPresent()){
            return true;
        }
        else {
            return false;
        }
    }
}
