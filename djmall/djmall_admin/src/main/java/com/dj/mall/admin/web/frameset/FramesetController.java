package com.dj.mall.admin.web.frameset;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FramesetController {

    @RequestMapping("toIndex")
    public String toIndex(){
        return "frameset/index";
    }
    @RequestMapping("toTop")
    public String toTop(){
        return "frameset/top";
    }
    @RequestMapping("toRight")
    public String toRight(){
        return "frameset/right";
    }
    @RequestMapping("toLeft")
    public String toLeft(){
        return "frameset/left";
    }
}
