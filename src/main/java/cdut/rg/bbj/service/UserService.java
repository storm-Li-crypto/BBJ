package cdut.rg.bbj.service;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    Result login(String account, String password, String code, HttpServletRequest request);

    Result register(HttpServletRequest request, User loginUser);


    Result change(String oldPassword, String newPasswordOne, String newPasswordTwo);
}
