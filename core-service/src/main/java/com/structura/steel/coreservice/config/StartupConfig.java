package com.structura.steel.coreservice.config;

import com.structura.steel.coreservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class StartupConfig{
    private final UserService userService;

    @EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
    public void initData() {
        userService.syncAllUsersFromKeycloakIfDbEmpty();
    }
}
