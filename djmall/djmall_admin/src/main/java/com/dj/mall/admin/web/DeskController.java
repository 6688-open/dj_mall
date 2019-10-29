package com.dj.mall.admin.web;


import com.dj.mall.common.ResultModel;
import com.dj.mall.util.HttpClientUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/desk/")
public class DeskController {

    @GetMapping("deskBase")
    public ResultModel<Object> deskBase(Integer userId){
        try {
            Map<String, String> params = new HashMap<>();
            params.put("pageNo","1");
            String s = HttpClientUtil.sendHttpRequest("http://192.168.2.175:8090/admin/dict/dict", HttpClientUtil.HttpRequestMethod.POST, params);
            return new ResultModel<>().success(s);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("服务器异常");
        }
    }

}
