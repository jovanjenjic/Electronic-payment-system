package com.ws.sep.paypalservice.model;

import com.ws.sep.paypalservice.enums.SubscriptionState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /* id of the agreement inside paypal */
    private String agreementId;

    private String name;

    private String description;

    /* current state of subscription */
    @Enumerated(EnumType.STRING)
    private SubscriptionState state;

    @CreatedDate
    private LocalDateTime createdAt;

}
