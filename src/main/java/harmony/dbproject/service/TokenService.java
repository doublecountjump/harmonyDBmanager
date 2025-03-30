package harmony.dbproject.service;


import harmony.dbproject.domain.Token;
import harmony.dbproject.error.ErrorCode;
import harmony.dbproject.error.ErrorException;
import harmony.dbproject.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Cacheable
 * 캐시에서 해당 키의 데이터가 존재한다면, 메서드 본문을 실행하지 않고 데이터를 반환
 */
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    @CachePut(value = "tokenCache", key = "#result.key")
    public Token generatedToken(){
        Token token = new Token(UUID.randomUUID());
        return tokenRepository.save(token);
    }

    @Cacheable(value = "tokenCache", key = "#key", unless = "#result == null")
    public Token findToken(String key){
        return tokenRepository.findTokenByKey(key).orElseThrow(() -> new ErrorException(ErrorCode.TOKEN_IS_NOT_VALID));
    }

    public boolean isTokenExists(String key){
        return findToken(key) != null;
    }

    public boolean validateApi_Key(String key) throws ErrorException{
        Token token = findToken(key);
        if(isTokenValid(token)){
            return true;
        }
        else return false;
    }

    private boolean isTokenValid(Token token) {
        return token.getExpiredDate().isAfter(LocalDateTime.now());
    }
}
