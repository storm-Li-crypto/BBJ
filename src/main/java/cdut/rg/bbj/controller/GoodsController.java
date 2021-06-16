package cdut.rg.bbj.controller;

import cdut.rg.bbj.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    // 获取商品
    @RequestMapping ( value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public Map<String,Object> getAll(@RequestParam String title, @RequestParam Integer page, @RequestParam String pnumber, @RequestParam String cnumber) {
        Map<String,Object> map = goodsService.getAll(title, page, pnumber, cnumber);
        return map;

    }

    // 商品详细情况
    @RequestMapping ( value = "/getCompare", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public Map<String, Object> getCompare(@RequestParam Integer goodId) {
        Map<String, Object> map = goodsService.getCompare(goodId);
        return map;
    }

}
