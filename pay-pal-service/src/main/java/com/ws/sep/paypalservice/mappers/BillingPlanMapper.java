package com.ws.sep.paypalservice.mappers;

import com.ws.sep.paypalservice.dto.BillingPlanResponse;
import com.ws.sep.paypalservice.model.BillingPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillingPlanMapper {

    BillingPlanMapper INSTANCE = Mappers.getMapper( BillingPlanMapper.class );

    @Mapping(source="planId", target = "paypalPlanId")
    BillingPlanResponse planToResponse(BillingPlan billingPlan);

}
