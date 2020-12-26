package com.ws.sep.netflixzuulapigatewayserver.logging;

import javax.servlet.http.HttpServletRequest;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ZullLoggingFilter extends ZuulFilter
{

    @Override
    public Object run() throws ZuulException
    {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        RestTemplate restTemplate = new RestTemplate();
        String resource = request.getRequestURI().toString();
        String path = "https://localhost:8084" + resource;

        if ( resource.contains( "/api/auth/signin" ) )
        {
            // everybody can sign in
            return null;
        }

        else if ( resource.contains( "/api/auth/signup" ) )
        {
            path = "https://localhost:8084/api/auth/signup";
        }

        try
        {

            String header = request.getHeader( "Authorization" );
            HttpHeaders headers = new HttpHeaders();
            headers.add( "Authorization", header );

            // ! someone must die so that others can live
            HttpEntity< String > kamikazeEntity = new HttpEntity< String >( headers );

            ResponseEntity< Object > response = restTemplate.exchange( path, HttpMethod.GET, kamikazeEntity, Object.class );
            System.out.println( response.toString() );
        }
        catch ( Exception e )
        {
            // if(e instanceof )
            if ( e instanceof HttpClientErrorException )
            {

                RequestContext context = RequestContext.getCurrentContext();
                switch ( ( ( HttpClientErrorException ) e ).getRawStatusCode() )
                {
                    case 401:
                        context.unset();
                        context.setResponseStatusCode( HttpStatus.UNAUTHORIZED.value() );

                        return null;
                    case 403:
                        context.unset();
                        context.setResponseStatusCode( HttpStatus.FORBIDDEN.value() );

                    default:
                        return null;
                }
            }

        }

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
