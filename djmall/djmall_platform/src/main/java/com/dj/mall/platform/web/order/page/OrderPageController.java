package com.dj.mall.platform.web.order.page;


import com.dj.mall.api.order.OrderService;
import com.dj.mall.api.order.mall.AddressService;
import com.dj.mall.api.order.user.UserService;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.mall.entiy.Address;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Order;
import com.dj.mall.domain.mall.entiy.ShoppingCar;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/order/")
public class OrderPageController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    /**
     * 去我的订单
     * @return 返回页面
     */
    @RequestMapping("toOrderList")
    public String toOrderList(){
        return "order/order_list";
    }
    /**
     * 去订单确认界面  通过 proId  proSkuId  查询回显   和商品信息一样
     * Pamare pNum  根据pNum查询    var proId = $("#proSkuId").val();
     *                              var proSkuId = $("#id").val();
     * @return 返回页面
     */
    @RequestMapping("buyNowOrder")
    public String buyNowOrder(Integer num,Integer proId, Integer proSkuId,  ModelMap map,String token, Integer userId){
        try {
            ProductSku productSku = null;
            //不同的sku对应的价格不同  根据sku id查询  把价格重新赋值
            if(null != proSkuId){
                productSku = userService.findProductSkuById(proSkuId);
            }
            //计算好评率
            List<Common> commonList = userService.findCommonAllByProductId(proId);
            //查询商品详细信息
            Product productMsg = userService.findProductMsgById(proId);
            //productSku 不空  将对应sku价格赋值给产品
            if(null != productSku){
                productMsg.setPriceShow(productSku.getSkuPrice());
                productMsg.setRateShow(productSku.getSkuRate());
                //当商品中的proSkuId ==  Sku   的id时  默认被选中
                productMsg.setProSkuId(productSku.getId());
            }

            //根据userId 查询地址列表  userId  从redis
            List<Address> addressList = userService.findAddressByUserIdToOrderList(userId, token);
            map.put("addressList", addressList);
            map.put("productMsg", productMsg);
            map.put("num", num);
            map.put("proSkuId", proSkuId);
            map.put("total", commonList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "order/buy_now";
    }


    /**
     * 展示父级订单的信息  查询本人登录的订单 userId
     * Pamare pNum  根据parentNum查询
     * * @return 返回页面
     */
    @RequestMapping("toParentNumMsg")
    public String toParentNumMsg(String parentNum, ModelMap map, String token){
        try {
            Order order = new Order();
            //根据父级num 订单号 查询List集合
            //商品总钱数
            Integer allMoney = SystemConstant.NUMBER_ZERO;
            //应付总金额   包含邮费
            Integer totalMoney = SystemConstant.NUMBER_ZERO;
            //应付总金额   不含邮费
            Integer totalMoney1 = SystemConstant.NUMBER_ZERO;
            //总运费
            Integer totalFreight = SystemConstant.NUMBER_ZERO;
            Integer i= SystemConstant.NUMBER_ZERO;
            List<Order> orderList = orderService.getOrderByParentNumAndUserId(parentNum, token);
            for (Order order1 : orderList) {
                //生成编号
                ++i;
                order1.setBianhao(i);
                order.setParentNum(order1.getParentNum());
               /* order.setAddress(order1.getAddress());*/
                order.setDeliveryTime(order1.getDeliveryTime());
                //字符串表示
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                order.setDeliveryTimeShow(df.format(order1.getDeliveryTime()));
                order.setPayStatus(order1.getPayStatus());
                order.setPayTime(order1.getPayTime());
                order.setPayStatus(order1.getPayStatus());
                order.setAddress(order1.getAddress());
                //字符串表示
               /* order.setPayTimeShow(df.format(order1.getPayTime()));*/
                allMoney += order1.getProNum()*order1.getOriginalPrice();
                totalMoney1 += order1.getProNum()*order1.getActualPrice();
                totalFreight += order1.getFreightMoney();
            }
            //应付总金额  应付商品总金额 + 总邮费
            order.setAllMoney(totalMoney1);
            order.setTotalMoney(totalMoney1+totalFreight);
            order.setTotalFreight(totalFreight);
            map.put("order", order);
            map.put("orderList", orderList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "order/parent_num_order_msg";
    }
    /**
     * 展示子级订单的信息
     * 根据sonNum查询 一条数据
     * * @return 返回页面
     */
    @RequestMapping("toSonNumMsg")
    public String toSonNumMsg(String sonNum, ModelMap map, String token){
        try {
            Order order = orderService.getOrderBySonNum(sonNum);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            order.setDeliveryTimeShow(df.format(order.getDeliveryTime()));
            //已取消 没有付款时间  非空判断  待收货  已完成  除了已取消 都有支付时间
            if(order.getOrderSonStatus() != SystemConstant.NUMBER_SON_NUM_STATUS_FOUR){
                order.setPayTimeShow(df.format(order.getPayTime()));
            }
            order.setTotalMoney(order.getActualPrice()+order.getFreightMoney());
            map.put("order", order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "order/son_num_order_msg";
    }


    /**
     * 在我的订单里面  根据父级Id  去查询  去支付 首先展示  然后回显
     * @param parentNum  根据parentNum  查询所有的订单
     * @param map  回显前台
     * @return  返回页面
     */
    @RequestMapping("toPay")
    public String toPay(String parentNum, ModelMap map, String token, Integer userId){
        try {
            //根据userId 查询地址列表  userId  从redis
            List<Address> addressList = userService.findAddressByUserIdToOrderList(userId, token);
            //根据父级num  去查询
            //商品总钱数
            Integer allMoney = SystemConstant.NUMBER_ZERO;                              ;
            //应付总金额   包含邮费
            Integer totalMoney = SystemConstant.NUMBER_ZERO;
            //应付总金额   不含邮费
            Integer totalMoney1 = SystemConstant.NUMBER_ZERO;
            //总运费
            Integer totalFreight = SystemConstant.NUMBER_ZERO;
            //商品个数
            Integer size = SystemConstant.NUMBER_ZERO;
            List<Order> orderList = orderService.getOrderByParentNumAndUserId(parentNum, token);
            //组装数据 总价格 商品数  总应付价格 总邮费
            Order order = new Order();
            for (Order order1 : orderList) {
                allMoney += order1.getProNum()*order1.getOriginalPrice();
                totalMoney1 += order1.getProNum()*order1.getActualPrice();
                totalFreight += order1.getFreightMoney();
                size += order1.getProNum();

            }
            //应付总金额  应付商品总金额 + 总邮费
            order.setAllMoney(totalMoney1);
            order.setTotalMoney(totalMoney1+totalFreight);
            order.setTotalFreight(totalFreight);
            map.put("order", order);
            map.put("orderList", orderList);
            map.put("size",size);
            map.put("addressList",addressList);
            map.put("parentNum",parentNum);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "order/to_pay";
    }




    /**
     * 再次购买   加入到购物车
     * Pamare pNum  根据pNum查询
     * @return 返回页面
     */
    @RequestMapping("toBuyAgain")
    public String toBuyAgain(String orderSonNum, ModelMap map,String token){
        try {
           //根据订单表orderSonNum   查询订单 去确认提交
            Order order = orderService.getOrderBySonNum(orderSonNum);
            //组装数据 加入到购物车
            ShoppingCar shoppingCar = new ShoppingCar();
            shoppingCar.setUserId(order.getUserId());
            shoppingCar.setProductName(order.getProductName());
            shoppingCar.setOldPrice(order.getOriginalPrice());
            shoppingCar.setProSku(order.getSkuAttrName());
            shoppingCar.setProRate(order.getRate());
            shoppingCar.setProFreight(order.getFreightMoney());
            shoppingCar.setNewPrice(order.getActualPrice());
            shoppingCar.setProSkuId(order.getProSkuId());
            shoppingCar.setNum(order.getProNum());
            //保存购物车
            Integer proCarId = userService.insertShoppingCar(shoppingCar, token);
            map.put("proCarId", proCarId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pro/my_shopping_car";
    }


    /**
     * 根据子集订单  去评论
     * @param orderSonNum
     * @return
     */
    @RequestMapping("toCommon")
    public String toCommon(String orderSonNum, ModelMap map){
        map.put("orderSonNum", orderSonNum);
        return "order/common";
    }


    /**
     * 商家去回复    评论id  回复内容               回复类型  2
     * @return
     */
    @RequestMapping("toReply")
    public String toReply( Integer id, Integer productId, ModelMap map){
        map.put("commentId", id);
        map.put("productId", productId);
        return "order/reply";
    }

    /**
     * 调用支付宝成功成功
     * @return
     */
    @RequestMapping("aliPaySuccess")
    public String aliPaySuccess(String token, ModelMap map){
        map.put("token", token);
        return "order/aliPaySuccess";
    }
    
}
