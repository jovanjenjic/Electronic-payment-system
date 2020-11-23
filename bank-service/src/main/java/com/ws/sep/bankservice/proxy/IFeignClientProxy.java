package com.ws.sep.bankservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient( name = "netflix-zuul-api-gateway-server" )
public interface IFeignClientProxy
{

    @GetMapping( value = "/bitcoin-service/test2/" )
    public String messageBitcoin();

    @GetMapping( value = "/pay-pal-service/test1/" )
    public String messagePaypal();

}
