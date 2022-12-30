package com.example.indiproject.config;

import com.example.indiproject.repositories.AppUserRepository;
import com.example.indiproject.repositories.ItemRepository;
import com.example.indiproject.services.api.AppUserService;
import com.example.indiproject.services.impl.AppUserServiceImpl;
import com.example.indiproject.services.impl.ItemServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public AppUserServiceImpl appUserServiceImpl(AppUserRepository appUserRepository) {
        return new AppUserServiceImpl(appUserRepository);
    }

    @Bean
    public ItemServiceImpl itemServiceImpl(ItemRepository itemRepository, AppUserRepository appUserRepository) {
        return new ItemServiceImpl(itemRepository, appUserRepository);
    }
}
