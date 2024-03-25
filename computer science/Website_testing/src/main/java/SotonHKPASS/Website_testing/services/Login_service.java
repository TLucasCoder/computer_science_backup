package SotonHKPASS.Website_testing.services;

import java.util.List;
import SotonHKPASS.Website_testing.entity.Login_info;

public interface Login_service {
    public abstract void createLogin(Login_info info);

    public abstract void updateLoginInfo(String id, Login_info info);

    public abstract void deleteLoginInfo(String id);

    public abstract List<Login_info> getLogin();

}