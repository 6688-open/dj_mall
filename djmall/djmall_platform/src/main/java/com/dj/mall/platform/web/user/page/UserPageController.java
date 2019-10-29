package com.dj.mall.platform.web.user.page;

import com.dj.mall.api.order.product.ProductService;
import com.dj.mall.api.order.user.UserService;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.mall.entiy.Address;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import com.dj.mall.domain.user.entiy.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/user/")
public class UserPageController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    /**
     * 去登录
     * @return
     */
    @RequestMapping("toLogin")
    public String toLogin(){
        return "user/login";
    } /**
     * 手机号码去登录
     * @return
     */
    @RequestMapping("phoneLogin")
    public String phoneLogin(){
        return "user/phone_login";
    }
    /**
     * 验证码
     * @return
     */
    @RequestMapping("toLoginImg")
    public String toLoginImg(String name, ModelMap map){
        map.put("name", name);
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
     * 登录成功到首页面  带token
     * @return
     */
    @RequestMapping("toIndex")
    public String toIndex(ModelMap map){

        try {
            //platform pom文件里依赖 product_api 调 productService.findDictionaryByProductType()的方法
            //platform 消费者文件里暴露productService接口
            List<Dictionary> dictionaryList = productService.findDictionaryByProductType();
            map.put("dictionaryList", dictionaryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pro/index";
    }


    /**
     *  个人信息详情
     * @return
     */
    @RequestMapping("toUpdateMsg")
    public String toUpdateMsg(String token, ModelMap map){
        try {
            map.put("user",  userService.toUpdateMsg(token));
            map.put("token", token);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pro/own_center";
    }





    /**
     * 点击进入商品详情             进入评价展示页面
     * @param  id 根据 id 查询
     * @return 跳转路径  带数据
     */
    @RequestMapping("toProductMessage")
    public String toProductMessage( Integer id,  ModelMap map, Integer skuId){
        ProductSku productSku = null;
        try {
            //不同的sku对应的价格不同  根据sku id查询  把价格重新赋值
            if(null != skuId){
                productSku = userService.findProductSkuById(skuId);
            }
            //查询商品详细信息
            Product productMsg = userService.findProductMsgById(id);
            //productSku 不空  将对应sku价格赋值给产品
            if(null != productSku){
                productMsg.setPriceShow(productSku.getSkuPrice());
                productMsg.setRateShow(productSku.getSkuRate());
                //当商品中的proSkuId ==  Sku   的id时  默认被选中
                productMsg.setProSkuId(productSku.getId());
            }
            //计算好评率
            List<Common> commonList = userService.findCommonAllByProductId(id);
            //查询评论 根据product id 查询
            int num = SystemConstant.NUMBER_ZERO; //0
            for (Common common1 : commonList) {
                //字符串表示 时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                common1.setCreateTimeShow(df.format(common1.getCreateTime()));
                //计算好评率
                if(common1.getScore() == SystemConstant.FOUR || common1.getScore() == SystemConstant.FIVE){ //4   5
                    num++;
                }
            }
            Double goodRate =(double) num/commonList.size();
            //四舍五入
            Double a = (double)Math.round(goodRate*100) ;
            //评论好评率
            map.put("goodRate", a);
            //总评价
            map.put("total",  commonList.size());


            //查询商品队形 sku  属性值
            map.put("productMsg", productMsg);
            List<ProductSku> skuProductNamesById = userService.findProductSkuByProductId(id);
            map.put("skuProductNamesById", skuProductNamesById);
            map.put("productId", id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pro/product_message";
    }



    /**
     * 进入我的购物车
     * @return
     */
    @RequestMapping("myShoppingCar")
    public String myShoppingCar(Integer proCarId, ModelMap map){
        map.put("proCarId", proCarId);
        return "pro/my_shopping_car";
    }

    /**
     * 进入确认订单确认列表    订单确认就是待支付的                 订单表存入数据  就是待支付的
     * @return idsInteger  购物车ids                 ids 购物车ids字符串      一个用来回显  一个用来查询  相同的数据
     */
    @RequestMapping("toMyOrder")
    public String toMyOrder(Integer [] idsInteger,String  ids, ModelMap map,String token, Integer userId){
        try {
            //根据userId 查询地址列表  userId  从redis
            List<Address> addressList = userService.findAddressByUserIdToOrderList(userId, token);
            map.put("ids", ids);
            map.put("addressList", addressList);
            map.put("idsInteger",idsInteger);
           /* map.put("pNum",pNum);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "order/my_order_sure";
    }




}
