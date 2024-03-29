package com.ws.sep.literalnoudruzenje.dto;

import com.ws.sep.literalnoudruzenje.model.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDTO {

    private Long id;

    private Double price;

    private String name;

    private String description;

    private Long user_id;

    private ItemType itemType = ItemType.MAGAZINE;

}
