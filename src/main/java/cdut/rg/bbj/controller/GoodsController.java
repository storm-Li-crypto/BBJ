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

    @RequestMapping ( value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public Map<String,Object> getAll(@RequestParam String title, @RequestParam Integer page) {
        Map<String,Object> map = goodsService.getAll(title, page);
        return map;

    }

}
