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
    @CrossOrigin(origins = "*",maxAge = 3600)
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
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Map<String,Object> login (HttpServletRequest request, @RequestBody Map<String, Object> map) {
        Object object = map.get("user");
        JSONObject jsonpObject = JSONObject.fromObject(object);
        User loginUser = (User) JSONObject.toBean(jsonpObject, User.class);
        String code = (String) map.get("code");
        Map<String,Object> loginMap = userService.login(request, loginUser, code);
        return loginMap;
    }

    // 给邮箱发送验证码
    @RequestMapping ( value = "/sendMail", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Result sendMail(HttpServletRequest request, @RequestBody Map<String, String> map) {
        String userTel = map.get("userTel");
        Result result = userService.sendMail(request, userTel);
        return result;
    }


    // 注册
    @RequestMapping ( value = "/register", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Result register (HttpServletRequest request, @RequestBody Map<String,Object> map) {
        Object object = map.get("user");
        JSONObject jsonpObject = JSONObject.fromObject(object);
        User loginUser = (User) JSONObject.toBean(jsonpObject, User.class);
        String emailCode = (String) map.get("emailCode");
        Result result = userService.register(request, loginUser, emailCode);
        return result;
    }

    // 更改密码
    @RequestMapping ( value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Result changePassword (HttpServletRequest request, @RequestBody Map<String, String> map) {
        Result result = userService.changePassword(request, map.get("oldPassword"), map.get("newPassword"), map.get("scdPassword"));
        return result;
    }

    // 获得用户信息
    @RequestMapping ( value = "/getInformation", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public User getInformation(HttpServletRequest request) {
        User user = userService.getUser(request);
        return user;
    }

    // 更改用户信息
    @RequestMapping ( value = "/changeInformation", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Result getInformation(HttpServletRequest request, @RequestBody User loginUser) {
        Result result = userService.changeInformation(request, loginUser);
        return result;
    }

    // 找回密码
    @RequestMapping ( value = "/find", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Result find(HttpServletRequest request, @RequestBody Map<String, Object> map) {
        String userTel = (String) map.get("userTel");
        Result result = userService.findPassword(request, (String) map.get("userAccount"), userTel, (String) map.get("newPassword"), (String) map.get("scdPassword"), (String) map.get("emailCode"));
        return result;
    }

    // 返回用户收藏
    @RequestMapping ( value = "/getCollect", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Result getLinkList(HttpServletRequest request) {
        User user = userService.getUser(request);
        Result result = userGoodsService.getLinkList(user);
        return result;
    }
}
