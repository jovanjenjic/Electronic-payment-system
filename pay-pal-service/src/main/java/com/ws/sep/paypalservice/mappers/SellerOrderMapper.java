package com.ws.sep.paypalservice.mappers;

import com.ws.sep.paypalservice.dto.OrderResponse;
import com.ws.sep.paypalservice.model.SellerOrders;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SellerOrderMapper {

    SellerOrderMapper INSTANCE = Mappers.getMapper( SellerOrderMapper.class );

    OrderResponse mapToResponse(SellerOrders order);

}
