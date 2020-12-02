package com.ws.sep.seller.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


import org.springframework.data.annotation.CreatedDate;


import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Seller
{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    // @NotBlank
    // @NotNull
    // @Size( min = 3, message = "password must have minimum 3 characters" )
    private String password;

    // @Email( message = "email must be valid" )
    // @Size( max = 100, message = "email can have maximum 100 characters" )
    // @NotBlank
    private String email;

    // @NotBlank
    // @NaturalId
    // @Size( min = 10000001, max = 99999999, message = "pib have to be in interval
    // [10000001, 99999999]" )
    private Long pib;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "user_roles", joinColumns = @JoinColumn( name = "user_id" ), inverseJoinColumns = @JoinColumn( name = "role_id" ) )
    private Set< Role > roles = new HashSet< Role >();

    @CreatedDate
    @Column( nullable = false, updatable = false )
    private LocalDateTime createdAt;

}
