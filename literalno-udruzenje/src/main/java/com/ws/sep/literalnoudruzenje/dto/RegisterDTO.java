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

    @Length(min = 4)
    private String name;

    @Email
    private String email;

    @Length(min = 4)
    private String password;

    @Positive
    private Long pib;

}
