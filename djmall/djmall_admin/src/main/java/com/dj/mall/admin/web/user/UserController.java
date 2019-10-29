package com.dj.mall.admin.web.user;

import com.dj.mall.api.order.user.UserService;
import com.dj.mall.common.ResultModel;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.mall.entiy.Order;
import com.dj.mall.domain.user.entiy.User;
import com.dj.mall.domain.user.vo.UserVo;
import com.dj.mall.util.PasswordSecurityUtil;
import com.dj.mall.util.VerifyCodeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param userVo  接收参数  用户名 手机号  邮箱  和密码 均可登录
     * @return
     */
    @PostMapping("login")
    public ResultModel login(UserVo userVo, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        try {
            User user =new User();
            BeanUtils.copyProperties(userVo, user);
            User user1 = userService.findUserByUsernameAndPassword(user);
            if(null == user1){
                return new ResultModel<>().error("用户名不存在");
            }
            //判断是否为重置最初密码   重置密码  1  未重置密码  2
            if(user1.getIsReset().equals(SystemConstant.ACTIVATE_ONE)){
                String password = PasswordSecurityUtil.enCode32(user1.getRestPassword());
                if(!password.equals(user.getPassword())){
                    return new ResultModel<>().error("密码错误");
                }
                map.put("id", user1.getId());
                return new ResultModel<>().error(300,"当前为重置密码 请修改",map);
            }

            //MD5判断密码是否一致
            /*String password = PasswordSecurityUtil.enCode32(user1.getPassword());
            if(!password.equals(user.getPassword())){
                return new ResultModel<>().error("密码错误");
            }*/

            //加盐    MD5  加盐 判断密码是否一致
            String salt = user1.getSalt();
            //前台加密后 +  盐  ----->加密    最终密码
            String password = PasswordSecurityUtil.generatePassword(user.getPassword(), salt);
            if(!user1.getPassword().equals(password)) {
                return new ResultModel<>().error("密码输入错误");
            }
            //商户激活状态   未激活不允许登录  1 商户  2 管理员   1 正常 2 未激活
            if(user1.getLever().equals(SystemConstant.LEVER_NUMBER_ONE) && user1.getStatus().equals(SystemConstant.STATUS_NUMBER_TWO)){
                return new ResultModel<>().error("该商户未激活");
            }
            //不允许买家登录
            if(user1.getLever().equals(SystemConstant.LEVER_NUMBER_THREE)){
                return new ResultModel<>().error("请前去商城平台登录页");
            }
            // 登录成功 存session
            session.setAttribute("username", userVo.getUsername());
            session.setAttribute("user1", user1);
            return new ResultModel().success("登陆成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }
    /**
     * 注册去重
     * @param username 用户名
     * @param phone  手机号
     * @param email  邮箱
     * @return
     */
    @RequestMapping("uniq")
    public Boolean uniq(String username, String phone, String email, String nickName){
        try{
            User user = userService.findUserToUniq(username, phone, email, nickName);
            //判断是否有值
            if(null != user){
                return false;//失败
            }
            return true;//成功
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 用户注册
     * @param  userVo 接收参数
     * @return
     */
    @PostMapping("register")
    public ResultModel register(UserVo userVo){
        try {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            // 判断是否为商户
            // 商户 1  管理员 2  正常状态 1 未激活状态 2
            if(user.getLever() == SystemConstant.STATUS_NUMBER_TWO){
                user.setStatus(SystemConstant.STATUS_NUMBER_ONE);
            }else{
                user.setStatus(SystemConstant.STATUS_NUMBER_TWO);
            }
            //生成保存盐
            String salt = PasswordSecurityUtil.generateSalt();
            String overPassword = PasswordSecurityUtil.generatePassword(user.getPassword(), salt);
            user.setPassword(overPassword);
            user.setSalt(salt);
            Integer userId = userService.addUserToRegister(user);
            //级别为1    用户  发送邮件
            // 判断 邮箱 分类 发送
            if(user.getEmail().contains("@163.com") && user.getLever().equals(SystemConstant.LEVER_NUMBER_ONE)){
                return new ResultModel().error(SystemConstant.EMAIL_CODE_TOW_ZERO_THREE,"注册成功 163邮箱查收",userId);
            } else if (user.getEmail().contains("@qq.com") && user.getLever().equals(SystemConstant.LEVER_NUMBER_ONE)){
                return new ResultModel().error(SystemConstant.EMAIL_CODE_TOW_ZERO_FOUR,"注册成功 qq邮箱查收",userId);
            }
            return new ResultModel().error(SystemConstant.EMAIL_CODE_TOW_ZERO_FIVE,"注册成功 ",userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }
    /**
     *忘记密码  图形验证码
     */
    //生成图形验证码
    @RequestMapping("getVerifCode")
    public void getVerifCode(HttpSession session, HttpServletResponse response) {
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        try {
            //生成验证码
            String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_ALL_MIXED, 4, null);
            session.setAttribute("verifyCode", verifyCode);
            //设置输出的内容的类型为JPEG图像
            BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);
            //写给浏览器
            ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 图形验证码是否相同
     * @param userVo  接收参数
     * @param session  将验证码存session
     * @return
     */
    @PostMapping("verifCode")
    public ResultModel verifCode(UserVo userVo, HttpSession session){
        //验证码非空判断
        if(StringUtils.isEmpty(userVo.getVerifCode())) {
            return new ResultModel<>().error("验证码不能为空");
        }
        //非空判断完成  判断验证码是否正确     equalsIgnoreCase 忽略大小写
        if(!((String) session.getAttribute("verifyCode")).equalsIgnoreCase(userVo.getVerifCode())   ) {
            return new ResultModel<>().error("验证码错误");
        }
        return new ResultModel<>().success("验证成功");

    }
    /**
     * 获取手机验证码
     * @param userVo  接收参数
     * @return
     */
    //手机短信验证phoneLogin
    @RequestMapping("getPhoneMsg")
    public ResultModel phoneLogin(UserVo userVo, HttpSession session){
        try {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            //忘记密码 codeType  1  代表忘记密码类型
            user.setCodeType(SystemConstant.CODETYPE_ONE);
            userService.saveAndRemove(user);
            //存session   codeType redis获取时 键
            session.setAttribute("codeType", user.getCodeType());
            return new ResultModel().success("验证码已发送到手机");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }
    /**
     * 忘记密码  发送邮箱
     * @param userVo  接收参数
     * @param code 判断验证码
     * @return
     */
    @RequestMapping("resetPsaaword")
    public ResultModel resetPsaaword(UserVo userVo, String code, String pwd, HttpSession session){
        try {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            //获取session中的键
            String codeType = String.valueOf(session.getAttribute("codeType"));
            user.setCodeType(codeType);
            userService.updateResetPasswordAndSendEmail(user, code, pwd);
            return new ResultModel().success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }

    }
    /**
     * 用户列表展示
     * @param pageNo 当前页
     * @param userVo 接收参数
     * @return
     */
    @PostMapping("list")
    public ResultModel<Object> list(Integer pageNo, UserVo userVo){
        try {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            return new ResultModel<>().success(userService.findUserAll(pageNo, user));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }
    }
    /**
     * 批量删除
     * @param ids  id 的数组 批量删除
     * @return
     */
    @RequestMapping("delUser")
    public ResultModel delUser(Integer [] ids){
        try {
            userService.delMoreUser(ids);
            return new ResultModel<>().success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }
    }

    /**
     * 批量激活
     * @param ids    id 的数组 批量激活
     * @return
     */
    @RequestMapping("status")
    public ResultModel status(Integer [] ids){
        try {
            userService.updateStatus(ids);
            return new ResultModel<>().success("激活成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }
    }
    /**
     * 修改用户
     * @param userVo 接收参数
     * @return
     */
    @PostMapping("update")
    public ResultModel update(UserVo userVo){
        try {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }
        return new ResultModel<>().success("修改成功");
    }

    /**
     * 重置密码 发送邮件  随机6位数
     * 如果用户登录时使用的是系统后台重置后的密码，强制要求用户进行密码修改。
     * @param id
     * @return
     */
    @RequestMapping("restPassword/{id}")
    public ResultModel restPassword(@PathVariable("id") Integer id){
        try {
            userService.updatePasswordAndSendEmail(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }
        return new ResultModel<>().success("重置密码成功");

    }

    /**
     * 当使用重置密码登陆时  去修改密码
     * @param userVo  接收参数
     * @return
     */
    @RequestMapping("upRestPassword")
    public ResultModel upRestPassword(UserVo userVo){
        try {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            userService.updateAndRestPasswordUpdateIsReset(user);
            return new ResultModel<>().success("重置密码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }

    }




    /**
     * 订单展示列表  商户  管理员页面
     * @param
     * @return
     */
    @RequestMapping("orderList")
    public ResultModel orderList(Integer lever, Integer sellerId){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Order> list = userService.findOrderAll(lever, sellerId);
            map.put("list", list);
            return new ResultModel<>().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }

    }

    /**
     * 卖家去发货 将待发货2   修改成已发货，待收货状态  3
     * @param orderSonNum  子集订单
     * @param message  待收货
     * @return
     */
    @RequestMapping("fahuoMsg")
    public ResultModel fahuoMsg(String orderSonNum, Integer message){
        try {
            userService.updateMessage(orderSonNum, message);
            return new ResultModel<>().success("");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }
    }

































}
