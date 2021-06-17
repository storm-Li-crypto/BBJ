package cdut.rg.bbj.controller;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.UserGoodsService;
import cdut.rg.bbj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired//自动装配
    private UserService userService;

    @Autowired//自动装配
    private UserGoodsService userGoodsService;

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
    @RequestMapping ( value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Result changePassword (HttpServletRequest request, @RequestBody Map<String, String> map) {
        Result result = userService.changePassword(request, map.get("oldpassword"), map.get("newpassword"), map.get("scdpassword"));
        return result;
    }

    // 获得用户信息
    @RequestMapping ( value = "/getInformation", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public User getInformation(HttpServletRequest request) {
        User user = userService.getUser(request);
        return user;
    }

    // 更改用户信息
    @RequestMapping ( value = "/changeInformation", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Result getInformation(HttpServletRequest request, @RequestBody User loginUser) {
        Result result = userService.changeInformation(request, loginUser);
        return result;
    }

    // 找回密码
    @RequestMapping ( value = "/find", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public boolean find(@RequestBody User loginUser) {
        return true;
    }

    // 返回用户收藏
    @RequestMapping ( value = "/getCollect", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Result getLinkList(HttpServletRequest request) {
        User user = userService.getUser(request);
        Result result = userGoodsService.getLinkList(user);
        return result;
    }
}
