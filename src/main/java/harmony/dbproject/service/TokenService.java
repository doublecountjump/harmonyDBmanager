package harmony.dbproject.service;


import harmony.dbproject.domain.Token;
import harmony.dbproject.error.ErrorCode;
import harmony.dbproject.error.ErrorException;
import harmony.dbproject.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @Cacheable
 * 캐시에서 해당 키의 데이터가 존재한다면, 메서드 본문을 실행하지 않고 데이터를 반환
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final TokenRepository tokenRepository;
    private final CacheManager manager;

    //토큰을 생성 후 캐시에 저장
    //@CachePut(value = "tokenCache", key = "#result.key") -> 내부참조에서는 왜 안되는가?
    public Token generatedToken(){
        Token token = new Token(UUID.randomUUID());
        Cache tokenCache = manager.getCache("tokenCache");
        if(tokenCache != null){
            tokenCache.put(token.getKey(), token);
        }else{
            log.warn("tokenCache is not defined!");
        }
        return tokenRepository.save(token);
    }

    /**
     *  key 에 해당하는 토큰을 찾아서 반환
     *  캐시에 있으면 캐시에서 반환, 없으면 db 조회 후 캐시에 저장
     * @param key 조회할 객체의 키
     * @return 존재하면 token, 없으면 null
     */
    //@Cacheable(value = "tokenCache", key = "#key", unless = "#result == null")
    public Token findToken(String key){
        Cache cache = manager.getCache("tokenCache");
        if(cache == null){
            return tokenRepository.findTokenByKey(key).orElse(null);
        }

        Token token = cache.get(key, Token.class);
        if(token != null){
            return token;
        }

        Token result = tokenRepository.findTokenByKey(key).orElse(null);
        if(result != null){
            cache.put(result.getKey(), result);
        }

        return result;
    }

    public boolean isTokenExists(String key){
        return findToken(key) != null;
    }

    public boolean validateApi_Key(String key){
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
