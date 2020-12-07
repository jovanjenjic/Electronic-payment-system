package com.ws.sep.paypalservice.model;

import com.ws.sep.paypalservice.enums.PlanState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /* id of item for plan */
    private Long itemId;

    /* id of the seller that owns the plan */
    private Long sellerId;

    /* paypal identifier of the plan */
    private String planId;

    private String currency;

    private String value;

    @Enumerated(EnumType.STRING)
    private PlanState state;

}
