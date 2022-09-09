package com.bridgelabz.fundoouserservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserDto {
    private long id;
    private String fullName;
    private long mobile;
    private String emailId;
    private String profilePic;
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean isActive;
    private boolean isDeleted;
    private Date dateOfBirth;

}
