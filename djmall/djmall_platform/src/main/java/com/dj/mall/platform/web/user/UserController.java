package com.dj.mall.platform.web.user;

import com.dj.mall.api.order.cmpt.RedisService;
import com.dj.mall.api.order.user.UserService;
import com.dj.mall.common.ResultModel;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.mall.entiy.ShoppingCar;
import com.dj.mall.domain.mall.vo.ShoppingCarVo;
import com.dj.mall.domain.product.entiy.Great;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import com.dj.mall.domain.user.entiy.User;
import com.dj.mall.domain.user.vo.UserVo;
import com.dj.mall.util.HttpClientUtil;
import com.dj.mall.util.PasswordSecurityUtil;
import com.dj.mall.util.UUIDUtil;
import com.dj.mall.util.VerifyCodeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

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
            //只能买家登录  3
            if(user1.getLever() != SystemConstant.LEVER_NUMBER_THREE){
                return new ResultModel<>().error("信息不通过  买家平台");
            }
            //买家激活状态   未激活不允许登录  1 商户  2 管理员  3 买家   1 正常 2 未激活
            if(user1.getLever().equals(SystemConstant.LEVER_NUMBER_THREE) && user1.getStatus().equals(SystemConstant.STATUS_NUMBER_TWO)){
                return new ResultModel<>().error("该买家未激活");
            }
            System.out.println(user1.getHeadImg());
            // 登录成功 存session
            session.setAttribute("username", userVo.getUsername());
            session.setAttribute("user1", user1);

            String token = UUIDUtil.code();//生成token
            redisService.set(token, user1, SystemConstant.NUMBER_SIX_ZERO);//存入缓存
            // DJ+***  昵称
            Random random = new Random();
            String result="DJ";
            for(int i=0;i<3;i++){
                result+=random.nextInt(SystemConstant.NUMBER_TEN);
            }
            map.put("token", token);
            map.put("nickName", result);

            return new ResultModel().success(map);
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
            // 商户 1  管理员 2 买家 3  默认3  正常状态 1 未激活状态 2  默认状态 1

            //生成保存盐
            String salt = PasswordSecurityUtil.generateSalt();
            String overPassword = PasswordSecurityUtil.generatePassword(user.getPassword(), salt);
            user.setPassword(overPassword);
            user.setSalt(salt);
            //默认头像  默认 昵称
            user.setHeadImg("poxgj8cq6.bkt.clouddn.com/c5638773-fa40-452d-96ab-745370b2696d");
            String result = "DJ";
            Random random = new Random();
            for(int i=0;i<6;i++){
                result += random.nextInt(SystemConstant.NUMBER_TEN);
            }
            user.setNickName(result);
            Integer userId = userService.addUserToRegister(user);
            //级别为1    用户  发送邮件
            return new ResultModel().success("success");
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
     * 获取手机验证码     忘记密码    手机登录的验证码获取
     * @param userVo  接收参数
     * @return
     */
    //手机短信验证phoneLogin
    @RequestMapping("getPhoneMsg")
    public ResultModel getPhoneMsg(UserVo userVo, HttpSession session){
        try {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            //忘记密码 codeType  1  代表忘记密码类型      2 代表 手机登录验证码，
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
     * 手机号码登录验证  前台输入的验证码和 之前存的redis里的进行比较  一天3次  时间超时 验证
     * @param userVo
     * @param phoneCode
     * @return
     */
    @RequestMapping("loginPhone")
    public ResultModel loginPhone(UserVo userVo, String phoneCode){
        try {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            userService.findUserByPhoneAndRedisPhoneCode(user, phoneCode);
            return new ResultModel<>().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }
    }






















    /**
     * 通过solr 查询商品展示列表
     * @param pageNo  参数
     * @param product  传递参数
     * @return  返回map
     * @throws Exception
     */
    @PostMapping("productAndSkuShow")
    public ResultModel productAndSkuShow(Integer pageNo, Product product){
        try {
           /* Product product = new Product();

            BeanUtils.copyProperties(productVo, product);*/
            return new ResultModel().success(userService.findProductBySolr(pageNo,product));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }









   /* productAndSkuShow*/
    /**
     * 商城展示列表  首页
     * @param pageNo  当前页
     * @param
     * @return 返回数据
     */
   /* @PostMapping("productAndSkuShow")
    public ResultModel productAndSkuShow(Integer pageNo, ProductVo productVo){
        Map<String, Object> map = new HashMap<>();
        try {
            Product product = new Product();

            BeanUtils.copyProperties(productVo, product);
            Page<Product> page = new Page<>(pageNo, SystemConstant.PAGESIZE_NUMBER);
            Page<Product> pageInfo = userService.findProductAndSKUAll(page, product);
            //分页结果
            map.put("list", pageInfo.getRecords());
            map.put("totalPage", pageInfo.getPages());
            map.put("pageNo", pageNo);

            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }*/

    /**
     * 修改  新增 个人信息   用户昵称   用户头像
     * @param file  文件 头像
     * @param userVo  传递参数
     * @return
     */
    @RequestMapping("updateUserMsg")
    public ResultModel updateUserMsg(MultipartFile file, UserVo userVo){
        try {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            //将file转成流的形式 传到serviceImpl层
            byte[] bytes = null;
            if(!file.isEmpty()){
                bytes = file.getBytes();
            }
            //保存修改
            userService.updateUserMsgById(bytes, user);
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 加入购物车    添加信息到购物车
     * @param shoppingCarVo  传递参数
     * @return
     */
    @RequestMapping("joinShoppingCar")
    public ResultModel joinShoppingCar(ShoppingCarVo shoppingCarVo, String token){
                    Map<String, Object> map = new HashMap<>();
        try {
            //非空判断
            if(     StringUtils.isEmpty(shoppingCarVo.getProductName()) ||
                    StringUtils.isEmpty(shoppingCarVo.getOldPrice())    ||
                    StringUtils.isEmpty(shoppingCarVo.getProFreight())  ||
                    StringUtils.isEmpty(shoppingCarVo.getProSku())      ||
                    StringUtils.isEmpty(shoppingCarVo.getProRate())     ||
                    StringUtils.isEmpty(shoppingCarVo.getProSkuId())
            ){
                return new ResultModel().error("不能有空项");
            }
            //根据sku  id  对应的库存 和前台传来的库存进行比较
            ProductSku sku = userService.findProductSkuById(shoppingCarVo.getProSkuId());
            //判断前台传来的库存   和sku表里库存进行比较
            //num 大于 库存 显示最大库存数
            if(sku.getSkuCount() == SystemConstant.NUMBER_ZERO){
                shoppingCarVo.setNum(sku.getSkuCount());
                return new ResultModel().error("无货");
            }
            if(shoppingCarVo.getNum() > sku.getSkuCount()){
                shoppingCarVo.setNum(sku.getSkuCount());
                return new ResultModel().error("库存不足， 库存仅剩"+sku.getSkuCount()+"件");
            }

            //添加购物车  同一用户   同一个proSkuId   没有的话是新增   有的话更新数量
            ShoppingCar shoppinggCar2 = userService.getShoppinngCarByUserIdAndProSkuId(token, shoppingCarVo.getProSkuId());
            if(shoppinggCar2 != null){
                //不为空 将前台传来的数量 和数据库的累加起来 执行修改操作
                shoppinggCar2.setNum(shoppinggCar2.getNum()+shoppingCarVo.getNum());
                //修改
                userService.updateShoppingCarNumByUserIdAndProSkuId(shoppinggCar2);
                map.put("proCarId",shoppinggCar2.getId());
                return new ResultModel().success("成功加入购物车",map);
            }

            //新增
            ShoppingCar shoppingCar = new ShoppingCar();
            BeanUtils.copyProperties(shoppingCarVo, shoppingCar);
            //最新价格的计算   原价 折扣 原价*折扣
            //如果折扣是0   现价就是原价
            if(shoppingCar.getProRate() == SystemConstant.NUMBER_ZERO){
                shoppingCar.setNewPrice(shoppingCar.getOldPrice());
            } else {
                shoppingCar.setNewPrice( shoppingCar.getOldPrice()*shoppingCar.getProRate()/SystemConstant.NUMBER_ONE_HOUNDER );
            }

            //新增  返回的是数据id
            Integer proCarId = userService.insertShoppingCar(shoppingCar, token);
            map.put("proCarId",proCarId);
            return new ResultModel().success("成功加入购物车",map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 购物车列表展示
     * @return
     */
    @RequestMapping("shoppingCarList")
    public ResultModel shoppingCarList(ShoppingCar shoppingCar, String token){
            Map<String, Object> map = new HashMap<>();
        try {
            List<ShoppingCar> list = userService.findShoppingCarById(shoppingCar, token);
            map.put("list", list);
            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 购物车删除已选中的商品  批量删除
     * @return  参数 ids
     */
    @RequestMapping("deleteShoppingCarByids")
    public ResultModel deleteShoppingCarByids(Integer [] ids){
        try {
            userService.deleteShoppingCarByIds(ids);
            return new ResultModel().success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 库存点击减少  或增加时 购物车更新数量
     * @param id 根据id
     * @param num
     * @return
     */
    @RequestMapping("updateNum")
    public ResultModel updateNum(Integer id, Integer num){
        HashMap<String, Object> map = new HashMap<>();
        try {
            //根据购物车id  去查询这条数据  对应的proSkuId   查询对应库存
            ShoppingCar shoppingCar = userService.getShoppingCarById(id);
            //根据sku  id  对应的库存 和前台传来的库存进行比较
            ProductSku sku = userService.findProductSkuById(shoppingCar.getProSkuId());
            //判断前台传来的库存   和sku表里库存进行比较
            //num 大于 库存 显示最大库存数  0
            if(sku.getSkuCount() == SystemConstant.NUMBER_ZERO){
                num = sku.getSkuCount();
                //根据购物车id 修改 数量
                userService.updateShoppingNumCarById(id, num);
                map.put("num",num);
                return new ResultModel().success("无货",map);
            }
            if(num > sku.getSkuCount()){
                num = sku.getSkuCount();
                userService.updateShoppingNumCarById(id, num);
                map.put("num",num);
                return new ResultModel().success("库存不足， 库存仅剩"+num+"件",map);
            }
                userService.updateShoppingNumCarById(id, num);
                map.put("num",num);
                return new ResultModel().success("有货，还可以购买"+sku.getSkuCount()+"件",map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }










    /**
     * 选中 立即结算  价格 邮费 商品个数 回显
     * @param ids  根据传来的ids进行查询
     * @return 返回数据
     * @throws Exception
     */
    @RequestMapping("checkCountAndMoney")
    public ResultModel checkCountAndMoney(Integer [] ids){
            Map<String, Object> map =new HashMap<>();
        try {
            List<ShoppingCar> shoppingCarList = userService.getShoppingCarByIds(ids);
            System.out.println(shoppingCarList.size());
            Integer totalMoney = SystemConstant.NUMBER_ZERO; // 总价格 0
            Integer shouldMoney = SystemConstant.NUMBER_ZERO; //折后价格0
            Integer totalFreight = SystemConstant.NUMBER_ZERO; //总邮费0
            Integer number = SystemConstant.NUMBER_ZERO ; //商品个数0
            for (ShoppingCar shoppingCar : shoppingCarList) {
                totalMoney += shoppingCar.getNum()*(shoppingCar.getOldPrice());
                shouldMoney += shoppingCar.getNum()*(shoppingCar.getNewPrice());
                totalFreight += shoppingCar.getProFreight();
                number += shoppingCar.getNum();
            }
            Integer finalMoney = shouldMoney + totalFreight;
            map.put("totalMoney", shouldMoney);
            map.put("finalMoney", finalMoney);
            map.put("totalFreight", totalFreight);
            map.put("number", number);
            map.put("ids", ids);
            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }



    /**
     * 确认订单展示列表
     * @return
     */
    @RequestMapping("myOrderSureList")
    public ResultModel shoppingCarList(Integer [] ids){
        Map<String, Object> map = new HashMap<>();
        try {
            List<ShoppingCar> list = userService.findShoppingCarByIds(ids);
            map.put("list", list);
            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

























    /**
     * 根据productId 和userId 查询 点赞表   点赞相关
     * @param token              id 商品id
     * @param proSkuId  thumbNumber
     * @return
     * @throws Exception
     */
    @RequestMapping("updateProduct")
    public ResultModel updateProduct(Integer id, String token, Integer proSkuId, Integer thumbNumber){
        try {
            //前台获取  proSkuId    点赞量 thumbNumber  如果已经点赞过就不能再点赞
            //还未点赞  就在点赞表 保存 proSkuId  userId  并修改 商品表的点赞量
            //查询是否 为空  空没有点赞    去保存点赞表  修改商品表   不空表示已经点赞过了
            Great great = userService.getGreatByUserIdAndProSkuId(token, proSkuId);
            if(null != great){
                //查询不空 说明已经点赞了 取消点赞  根据查询的id 进行删除
                userService.deleteGreatById(great.getId(), id, thumbNumber);
                //重做索引
                HttpClientUtil.sendHttpRequest("http://localhost:8081/solr/slorCoreDJmallProduct/dataimport?command=full-import", HttpClientUtil.HttpRequestMethod.GET, null);
                return new ResultModel().success("成功取消点赞");

               /* return new ResultModel<>().error("你已经对此商品点赞过了  不可重复点赞");*/
            }
            //如果没有点赞  去保存点赞表  修改商品表 点赞数量 前台传来的就是点赞量加 点击量量+1
            userService.addGreat(id, token, proSkuId, thumbNumber);
            //重做索引
            HttpClientUtil.sendHttpRequest("http://localhost:8081/solr/slorCoreDJmallProduct/dataimport?command=full-import", HttpClientUtil.HttpRequestMethod.GET, null);
            return new ResultModel().success("点赞成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error(e.getMessage());
        }
    }






}
