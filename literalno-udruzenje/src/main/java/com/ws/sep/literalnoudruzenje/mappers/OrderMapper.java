package com.ws.sep.literalnoudruzenje.mappers;

import com.ws.sep.literalnoudruzenje.dto.OrderListDTO;
import com.ws.sep.literalnoudruzenje.model.Item;
import com.ws.sep.literalnoudruzenje.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source="item", target = "itemName", qualifiedByName = "mapItemName")
    OrderListDTO mapOrderToListResponse(Order order);

    @Named("mapItemName")
    static String mapItemName(Item item) {
        return item.getName();
    }
}
