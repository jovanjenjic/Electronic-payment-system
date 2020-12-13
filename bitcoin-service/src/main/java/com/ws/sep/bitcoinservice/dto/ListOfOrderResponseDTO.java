package com.ws.sep.bitcoinservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListOfOrderResponseDTO {
    @JsonProperty(value = "current_page")
    private Integer currentPage;

    @JsonProperty(value = "per_page")
    private Integer perPage;


    @JsonProperty(value = "total_orders")
    private Integer totalOrders;

    @JsonProperty(value = "total_pages")
    private Integer totalPages;

    @JsonProperty(value = "orders")
    private List<ItemOfListOrderDTO> orders;

}