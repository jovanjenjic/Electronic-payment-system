package com.ws.sep.seller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest
{

    // @Email( message = "email must be valid" )
    // @Size( max = 100, message = "email can have maximum 100 characters" )
    // @NotBlank
    private String email;

    // @NotBlank
    // @Size( min = 10000001, max = 99999999, message = "pib have to be in interval [10000001, 99999999]" )
    private Long pib;

    // @NotBlank
    // @NotNull
    // @Size( min = 3, message = "password must have minimum 3 characters" )
    private String password;

}
