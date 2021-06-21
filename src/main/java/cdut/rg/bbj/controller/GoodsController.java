package cdut.rg.bbj.controller;

import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.GoodsService;
import cdut.rg.bbj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;

    // 获取商品
    @RequestMapping ( value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Map<String,Object> getAll(HttpServletRequest request, @RequestParam String title, @RequestParam Integer page, @RequestParam String pnumber, @RequestParam String cnumber) {
        User user = userService.getUser(request);
        Map<String,Object> map = goodsService.getAll(user, title, page, pnumber, cnumber);
        return map;
    }

    // 相似商品对比
    @RequestMapping ( value = "/getCompare", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin(origins = "*",maxAge = 3600)
    public Map<String, Object> getCompare(@RequestParam Integer goodId) {
        Map<String, Object> map = goodsService.getCompare(goodId);
        return map;
    }

}
