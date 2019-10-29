package com.dj.mall.platform.web.frameset;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FramesetController {

    @RequestMapping("toIndex")
    public String toIndex(String token, ModelMap map){
        map.put("token", token);
        return "frameset/index";
    }
    @RequestMapping("toTop")
    public String toTop(String token, ModelMap map){
        map.put("token", token);
        return "frameset/top";
    }
    @RequestMapping("toRight")
    public String toRight(String token, ModelMap map){
        map.put("token", token);
        return "frameset/right";
    }
    @RequestMapping("toLeft")
    public String toLeft(String token, ModelMap map){
        map.put("token", token);
        return "frameset/left";
    }
}
