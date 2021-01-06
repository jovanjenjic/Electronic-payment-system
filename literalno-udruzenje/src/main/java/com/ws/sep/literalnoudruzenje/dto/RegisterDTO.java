package com.ws.sep.literalnoudruzenje.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    @Length(min = 4, message = "name must have length greater or equal to 4")
    private String name;

    @Email(message = "please enter valid email")
    private String email;

    @Length(min = 4, message = "password must have length greater or equal to 4")
    private String password;

    @Positive(message = "pib must be positive")
    private Long pib;

}
