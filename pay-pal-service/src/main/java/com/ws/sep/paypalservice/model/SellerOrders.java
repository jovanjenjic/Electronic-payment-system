package com.ws.sep.paypalservice.model;

import com.ws.sep.paypalservice.enums.OrderState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SellerOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double price;

    private int itemsCount;

    private Long itemId;

    private Long orderId;

    private String currency;

    private String description;

    private String paymentUrl;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    private Long sellerId;

    private String paymentId;

    @CreatedDate
    private LocalDateTime createdAt;
}
