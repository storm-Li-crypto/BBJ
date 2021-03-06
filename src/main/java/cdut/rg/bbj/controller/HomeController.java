package cdut.rg.bbj.controller;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.GoodsService;
import cdut.rg.bbj.service.UserGoodsService;
import cdut.rg.bbj.service.UserService;
import cdut.rg.bbj.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserGoodsService userGoodsService;

    @RequestMapping ( value = "/getRecommendation", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Result getRecommendation(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String account = TokenUtil.getUserID(token);
        System.out.println(account);
        Result result = userGoodsService.getRecommendation(account);
        return result;
    }

    @RequestMapping ( value = "/changNumber", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Result changeLinks(HttpServletRequest request, @RequestBody Map<String,Integer> map) {
        User user = userService.getUser(request);
        Result result = userGoodsService.change(user, map.get("index"), map.get("iscollect"));
        return result;
    }

}