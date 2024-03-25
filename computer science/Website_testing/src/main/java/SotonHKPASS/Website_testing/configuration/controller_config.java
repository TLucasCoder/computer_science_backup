package SotonHKPASS.Website_testing.configuration;

import org.springframework.context.annotation.*;
import SotonHKPASS.Website_testing.implementation.Login_service_impl;
import SotonHKPASS.Website_testing.services.Login_service;
import SotonHKPASS.Website_testing.repository.Login_repository;

@Configuration
public class controller_config {
    private Login_repository Login_repository;

    @Bean
    public Login_service login_service() {
        return new Login_service_impl(Login_repository);
    }

}