package com.dj.mall.admin.web.user.page;

import com.dj.mall.api.order.user.UserService;
import com.dj.mall.domain.user.entiy.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/")
public class UserPageController {
    @Autowired
    private UserService userService;

    /**
     * 去登录
     * @return
     */
    @RequestMapping("toLogin")
    public String toLogin(){
        return "user/login";
    }
    /**
     * 验证码
     * @return
     */
    @RequestMapping("toLoginImg")
    public String toLoginImg(){
        return "user/login_img";
    }

    /**
     * 去注册
     * @return
     */
    @RequestMapping("toRegister")
    public String toRegister(){
        return "user/register";
    }

    /**
     * 忘记密码  手机号找回密码
     * @return
     */
    @RequestMapping("toFindPassword")
    public String toFindPassword(){
        return "user/find_password";
    }

    /**
     * 修改状态  激活用户
     * @param id  根据id 查询
     * @param map  返还数据
     * @return
     */
    @RequestMapping("updateStatus")
    public String updateStatus(Integer id, ModelMap map){
        try {
            userService.updateStatusToUse(id);
            map.put("success","激活成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user/login";
    }
    /**
     * 去用户展示
     * @return
     */
    @RequestMapping("toList")
    public String toList(){
        return "user/list";
    }

    /**
     * 去修改用户
     * @param id  根据id 修改
     * @param map 返还数据
     * @return
     */
    @RequestMapping("toUpdate/{id}")
    public String toUpdateUser(@PathVariable("id") Integer id, ModelMap map){
        try {
            User user = userService.toUpdateUser(id);
            map.put("user", user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user/update";
    }

    /**
     * 重置密码  强制修改密码
     * @return
     */
    @RequestMapping("reset/{id}")
    public String reset(@PathVariable("id") Integer id, ModelMap map ){
        map.put("id", id);
        return "user/reset";
    }



    /**
     * 去订单的展示列表
     * @return
     */
    @RequestMapping("toOrderList")
    public String toOrderList(){
        return "pro_order/list";
    }



}
