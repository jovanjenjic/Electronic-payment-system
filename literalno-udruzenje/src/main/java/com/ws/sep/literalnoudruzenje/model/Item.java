package com.ws.sep.literalnoudruzenje.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double price;

    private String name;

    // discount used for customer
    private Double discount = 10d;

    @Enumerated(EnumType.STRING)
    private ItemType itemType = ItemType.MAGAZINE;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

}
