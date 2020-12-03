package com.ws.sep.seller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest
{

    private String email;

    private Long pib;

    private String password;

}
