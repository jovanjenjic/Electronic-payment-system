package com.ws.sep.bitcoinservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer paymentId;
    private String status;
    private String priceAmount;
    private String priceCurrency;
    private String receiveCurrency;
    private LocalDateTime createdAt;
    private String orderId;

    @ManyToOne
    @JoinColumn(name="seller_info_id", nullable=false)
    private CryptocurrencyPayment cryptocurrencyPayment;
}
