package com.khouloud.auth.api.advice;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
     	var errorResponse = new ErrorResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Access Denied: You don't have permission to access this resource."
        );
     	 ObjectMapper mapper = new ObjectMapper();
         response.getWriter().write(mapper.writeValueAsString(errorResponse));
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
    }
}
