package SotonHKPASS.Website_testing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Index_controller {
    @RequestMapping("/")
    String index() {
        return "index";
    }

    @RequestMapping(value = "/flagship_login", method = RequestMethod.GET)
    String login() {
        return "index";
    }

}