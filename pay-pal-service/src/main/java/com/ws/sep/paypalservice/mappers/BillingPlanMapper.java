package com.ws.sep.paypalservice.mappers;

import com.ws.sep.paypalservice.dto.BillingPlanDTO;
import com.ws.sep.paypalservice.dto.BillingPlanResponse;
import com.ws.sep.paypalservice.model.BillingPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Locale;

@Mapper
public interface BillingPlanMapper {

    BillingPlanMapper INSTANCE = Mappers.getMapper( BillingPlanMapper.class );

    @Mapping(source="planId", target = "paypalPlanId")
    BillingPlanResponse planToResponse(BillingPlan billingPlan);

    @Mapping(source="value", target = "value", qualifiedByName = "mapValueToString")
    BillingPlan planFromDto(BillingPlanDTO billingPlanDTO);

    @Named("mapValueToString")
    static String mapValueToString(Double value) {
        return (value == null || value.isNaN()) ? "" : String.format(Locale.US,"%.2f", value);
    }
}
