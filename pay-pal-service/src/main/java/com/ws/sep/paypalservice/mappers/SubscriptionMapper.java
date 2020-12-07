package com.ws.sep.paypalservice.mappers;

import com.ws.sep.paypalservice.dto.SubscriptionResponse;
import com.ws.sep.paypalservice.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubscriptionMapper {

    SubscriptionMapper INSTANCE = Mappers.getMapper( SubscriptionMapper.class );

    SubscriptionResponse mapToResponse(Subscription subscription);

}
