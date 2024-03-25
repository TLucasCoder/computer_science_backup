package SotonHKPASS.Website_testing.implementation;

import java.util.*;

import SotonHKPASS.Website_testing.entity.Login_info;
import SotonHKPASS.Website_testing.repository.Login_repository;
import SotonHKPASS.Website_testing.services.Login_service;
import org.springframework.stereotype.Service;

@Service
public class Login_service_impl implements Login_service {
    private Login_repository login_repo;

    public Login_service_impl(Login_repository login_repo) {
        super();
        this.login_repo = login_repo;
    }

    @Override
    public void createLogin(Login_info info) {

    }

    @Override
    public void updateLoginInfo(String id, Login_info info) {

    }

    @Override
    public void deleteLoginInfo(String id) {

    }

    // find out a list of all login info
    @Override
    public List<Login_info> getLogin() {
        return login_repo.findAll();
    }
}
