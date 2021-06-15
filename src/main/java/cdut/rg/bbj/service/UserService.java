package cdut.rg.bbj.service;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserService {

    Map<String,Object> login(HttpServletRequest request, User loginUser);

    Result register(HttpServletRequest request, User loginUser);


    Result change(HttpServletRequest request, String oldPassword, String newPasswordOne, String newPasswordTwo);

    User getUser(HttpServletRequest request);
}
