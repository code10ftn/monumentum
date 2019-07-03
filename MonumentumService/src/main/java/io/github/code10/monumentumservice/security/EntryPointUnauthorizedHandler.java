package io.github.code10.monumentumservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.code10.monumentumservice.model.dto.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Entry point for returning HTTP response for unauthorised users.
 */
@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {


    private ObjectMapper jacksonObjectMapper;

    public EntryPointUnauthorizedHandler(ObjectMapper jacksonObjectMapper) {
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        final ErrorResponse errorResponse = new ErrorResponse("Access denied");


        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getOutputStream().println(jacksonObjectMapper.writeValueAsString(errorResponse));
    }
}

