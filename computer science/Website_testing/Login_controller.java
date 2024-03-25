package hkpass.website.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import hkpass.website.services.Login_service;
import hkpass.website.entity.Login_info;
import org.springframework.ui.Model;
//import hkpass.website.repository.login_repository;

@Controller
public class Login_controller {

    @Autowired
    private Login_service login_service;

    // get userName list
    @GetMapping("/testing")
    public List<Login_info> listUserInfo(Model model) {
        model.addAttribute("userName", login_service.getLogin());

        return login_service.getLogin();
    }

    // get userName list
    // @GetMapping("/testing")
    public List<Login_info> listPasswordInfo(Model model) {
        model.addAttribute("password", login_service.getLogin());
        return login_service.getLogin();
    }

    @GetMapping("/create_detail")
    void create(String userName, String password) {

    }

    @GetMapping("/login")
    String auth(String userName, String password, Model model) {
        // init
        boolean admin = false;
        boolean parti = false;
        List<Login_info> userList = login_service.getLogin();
        for (Login_info info : userList) {
            if (info.getUserName() == userName && info.getPassword() == password) {
                if (info.getAcc_type() == "admin") {
                    admin = true;
                } else if (info.getAcc_type() == "participant") {
                    parti = true;
                }
                break;
            }
        }
        if (admin) {
            return "parti dashboard";
        }
        if (parti) {
            return "helper dashboard";
        }
        // model.addAttribute();
        return "flagship_login";
    }

}
