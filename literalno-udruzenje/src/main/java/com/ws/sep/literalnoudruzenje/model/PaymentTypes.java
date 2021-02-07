package com.ws.sep.literalnoudruzenje.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class PaymentTypes {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( length = 60, unique = true)
    @Enumerated(EnumType.STRING)
    private PaymentType type;

}
