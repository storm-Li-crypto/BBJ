package cdut.rg.bbj.service;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserService {

    Map<String,Object> login(HttpServletRequest request, User loginUser, String code);

    Result register(HttpServletRequest request, User loginUser, String emailCode);


    Result changePassword(HttpServletRequest request, String oldPassword, String newPasswordOne, String newPasswordTwo);

    User getUser(HttpServletRequest request);

    Result changeInformation(HttpServletRequest request, User loginUser);

    Result findPassword(HttpServletRequest request, String userAccount, String userTel, String newPassword, String scdPassword, String emailCode);

//    Result sendMail(HttpServletRequest request, String userAccount, String userTel);
    Result sendMail(HttpServletRequest request, String userTel);
}
