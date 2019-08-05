package com.alextim.config;

import com.alextim.cache.Cache;
import com.alextim.cache.MyCache;
import com.alextim.domain.User;
import com.alextim.repository.UserRepository;
import com.alextim.repository.UserRepositoryImpl;
import com.alextim.service.ServiceDB;
import com.alextim.service.ServiceDbImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.concurrent.TimeUnit;

@Configuration
public class Config {

    @Bean
    public UserRepository getUserRepository() {
        return new UserRepositoryImpl();
    }

    @Bean
    public Cache<User> getCache() {
        return new MyCache<>(10,
                TimeUnit.SECONDS.toMillis(1),
                TimeUnit.SECONDS.toMillis(1));
    }

    @Bean
    public ServiceDB getServiceDB(UserRepository repository, Cache<User> cache) {
        return new ServiceDbImpl(repository, cache);
    }

    @Bean
    public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
}
