package com.ws.sep.netflixzuulapigatewayserver.logging;

import javax.servlet.http.HttpServletRequest;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;


import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ZullLoggingFilter extends ZuulFilter
{

    @Override
    public Object run() throws ZuulException
    {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        log.info( ">>>>>>>>>> request -> {}", request );
        return null;

    }


    @Override
    public boolean shouldFilter()
    {
        return true;

    }


    @Override
    public int filterOrder()
    {
        return 0;

    }


    @Override
    public String filterType()
    {
        return "pre";

    }

}
