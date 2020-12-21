package com.ws.sep.issuer.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "client" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client
{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Double availableFounds;

    private String cardHolder;

    private String cvv;

    private String mm;

    private String pan;

    private String yy;

}
