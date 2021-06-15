package cdut.rg.bbj.controller;


import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.UserService;
import cdut.rg.bbj.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/index")
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping ( value = "/check", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Map<String,Object> check (HttpServletRequest request, @RequestBody User loginUser) {
        String code="200";
        Map<String,Object> loginMap = new HashMap<>();
        Map<String,Object> dataMap = new HashMap<>();
        System.out.println("用户账号"+loginUser.getUserId());
        System.out.println("用户账号"+loginUser.getUserAccount());
        System.out.println("用户密码"+loginUser.getUserPassword());
        String token = TokenUtil.sign(loginUser.getUserAccount());//后端收到请求，验证用户名和密码，验证成功，就给前端返回一个token
        dataMap.put("token",token);
        loginMap.put("data",dataMap);
        Map<String,Object> metaMap = new HashMap<>();
        Result user = userService.login(loginUser.getUserAccount(),loginUser.getUserPassword(),code,request);
        if(user != null){
            metaMap.put("msg","登录成功");
            metaMap.put("status",200);
        } else {
            metaMap.put("msg","登录失败");
            metaMap.put("status",0);
        }
        loginMap.put("meta",metaMap);
        return  loginMap;
    }

    @RequestMapping ( value = "/giveform", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public int giveform() {
        return -1;
    }

    // 找回密码
    @RequestMapping ( value = "/find", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public boolean find(@RequestBody User loginUser) {
        return true;
    }

    @RequestMapping ( value = "/getInformation", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public String getInformation(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println("用户token"+token);
        String account = TokenUtil.getUserID(token);
        System.out.println("用户token"+account);
        return account;
    }
}