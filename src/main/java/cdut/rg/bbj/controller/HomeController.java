package cdut.rg.bbj.controller;


import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.GoodsService;
import cdut.rg.bbj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping ( value = "/getRecommendation", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Result getRecommendation(HttpServletRequest request) {
        User user = userService.getUser(request);
        Result result = goodsService.getRecommendation(user);
        return null;
    }




}