package com.ws.sep.netflixzuulapigatewayserver.logging;

import java.io.IOException;
import java.nio.file.AccessDeniedException;


import javax.servlet.http.HttpServletResponse;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler( AccessDeniedException.class )
    public void handleAccessDeniedException( HttpServletResponse res, AccessDeniedException e ) throws IOException
    {

        res.sendError( HttpStatus.FORBIDDEN.value(), "Access denied" );

    }

}
