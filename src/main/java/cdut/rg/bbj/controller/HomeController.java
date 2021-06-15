package cdut.rg.bbj.controller;


import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/index")
public class HomeController {

    @Autowired
    private UserService userService;



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

}