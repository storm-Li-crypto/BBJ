package cdut.rg.bbj.controller;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.UserGoodsService;
import cdut.rg.bbj.service.UserService;
import cn.dsna.util.images.ValidateCode;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired//自动装配
    private UserService userService;

    @Autowired//自动装配
    private UserGoodsService userGoodsService;

    @RequestMapping ( value = "/getCode", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public void getCode(HttpServletResponse response, HttpServletRequest request) {
        ValidateCode validateCode = new ValidateCode(120, 40, 4, 100);
        request.getSession().setAttribute("code", validateCode.getCode());
        try {
            // 把图片响应给客户端
            validateCode.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 登录
    @RequestMapping ( value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Map<String,Object> login (HttpServletRequest request, @RequestBody Map<String,Object> map) {
        Object object = map.get("user");
        JSONObject jsonpObject = JSONObject.fromObject(object);
        User loginUser = (User) JSONObject.toBean(jsonpObject, User.class);
        String code = (String) map.get("code");
        Map<String,Object> loginMap = userService.login(request, loginUser, code);
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
    public Result find(@RequestBody Map<String, String> map) {
        System.out.println("\033[47;4m" + map.get("userAccount") + "hhhhhh" + "\033[0m");
        System.out.println("\033[47;4m" + map.get("userAnswer") + "hhhhhh" + "\033[0m");
        System.out.println("\033[47;4m" + map.get("newPassword") + "hhhhhh" + "\033[0m");
        System.out.println("\033[47;4m" + map.get("scdPassword") + "hhhhhh" + "\033[0m");
        Result result = userService.findPassword(map.get("userAccount"), map.get("userAnswer"), map.get("newPassword"), map.get("scdPassword"));
        return result;
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
