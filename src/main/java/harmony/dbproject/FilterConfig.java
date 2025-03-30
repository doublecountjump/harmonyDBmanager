package harmony.dbproject;


import com.github.benmanes.caffeine.cache.Caffeine;
import harmony.dbproject.filter.TokenValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<TokenValidationFilter> tokenValidationFilterConfigure(TokenValidationFilter filter) {
        FilterRegistrationBean<TokenValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/graphql");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("tokenCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500));
        return cacheManager;
    }


}
