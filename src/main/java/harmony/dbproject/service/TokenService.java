package harmony.dbproject.service;


import harmony.dbproject.domain.Token;
import harmony.dbproject.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Token generatedToken(){
        Token token = new Token(UUID.randomUUID());
        return tokenRepository.save(token);
    }
}
