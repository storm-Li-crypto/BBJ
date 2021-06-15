package cdut.rg.bbj.controller;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.UserService;
import cdut.rg.bbj.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired//自动装配
     UserService userService;

    // 登录
    @RequestMapping ( value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Map<String,Object> login (HttpServletRequest request, @RequestBody User loginUser) {
        String code="200";
        Map<String,Object> loginMap = userService.login(request, loginUser);
        return loginMap;
    }

    // 注册
    @RequestMapping ( value = "/register", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Result register (HttpServletRequest request, @RequestBody User loginUser) {
        Result result = userService.register(request, loginUser);
        return result;
    }

    // 更改密码
    @RequestMapping ( value = "/change", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Result change (HttpServletRequest request, @RequestBody Map<String, String> map) {
        Result result = userService.change(request, map.get("oldpassword"), map.get("newpassword"), map.get("scdpassword"));
        return result;
    }

    @RequestMapping ( value = "/getInformation", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public User getInformation(HttpServletRequest request) {
        User user = userService.getUser(request);
        return user;
    }

}
