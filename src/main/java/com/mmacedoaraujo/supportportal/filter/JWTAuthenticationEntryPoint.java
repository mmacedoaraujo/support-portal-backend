package com.mmacedoaraujo.supportportal.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmacedoaraujo.supportportal.domain.HttpResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

import static com.mmacedoaraujo.supportportal.constant.SecurityConstant.FORBIDDEN_MESSAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Component
public class JWTAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        HttpResponse httpResponse = HttpResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .httpStatusCode(HttpStatus.FORBIDDEN.value())
                .reason(HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase())
                .message(FORBIDDEN_MESSAGE)
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}
