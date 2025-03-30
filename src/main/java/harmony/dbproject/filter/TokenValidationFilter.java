package harmony.dbproject.filter;


import ch.qos.logback.core.spi.ErrorCodes;
import com.fasterxml.jackson.databind.ObjectMapper;
import harmony.dbproject.error.ErrorCode;
import harmony.dbproject.error.ErrorException;
import harmony.dbproject.error.ErrorResponse;
import harmony.dbproject.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.View;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
//api키가 존재하는지 확인하는 필터
public class TokenValidationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String key = request.getHeader("api_key");
        if(tokenService.isTokenExists(key)){

            if(tokenService.validateApi_Key(key)){

                filterChain.doFilter(request,response);
            }
            else{
                log.warn("유효하지 않은 key 접근");
                ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TOKEN_IS_NOT_VALID);
                sendErrorResponse(response,errorResponse, HttpServletResponse.SC_UNAUTHORIZED);
            }

        }else{
            log.warn("존재하지 않는 key 접근");
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TOKEN_NOT_FOUND);
            sendErrorResponse(response, errorResponse, HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorResponse errorResponse, int code) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
