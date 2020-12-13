package com.ws.sep.bitcoinservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CryptocurrencyPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long sellerId;
    private String apiKey;

    @OneToMany(mappedBy="cryptocurrencyPayment")
    private Set<PaymentInformation> payments;
}
