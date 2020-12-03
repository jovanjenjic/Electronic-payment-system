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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


import org.springframework.data.annotation.CreatedDate;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table( name = "seller", uniqueConstraints =
{ @UniqueConstraint( columnNames =
{ "email" } ), @UniqueConstraint( columnNames =
{ "pib" } ) } )
public class Seller
{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String password;

    private String email;

    private Long pib;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "seller_roles", joinColumns = @JoinColumn( name = "seller_id" ), inverseJoinColumns = @JoinColumn( name = "role_id" ) )
    private Set< Role > roles = new HashSet< Role >();

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "seller_types", joinColumns = @JoinColumn( name = "seller_id" ), inverseJoinColumns = @JoinColumn( name = "type_id" ) )
    private Set< PaymentType > types = new HashSet< PaymentType >();

    @CreatedDate
    @Column( nullable = false, updatable = false )
    private LocalDateTime createdAt;

}
