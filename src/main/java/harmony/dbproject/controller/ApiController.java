package harmony.dbproject.controller;


import harmony.dbproject.domain.Token;
import harmony.dbproject.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final TokenService tokenService;

    @PostMapping("/issue")
    public Token requestApi(HttpServletRequest request){
        return tokenService.generatedToken();
    }
}
