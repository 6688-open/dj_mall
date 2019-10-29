package com.dj.mall.platform.web.order;

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.api.order.OrderService;
import com.dj.mall.api.order.user.UserService;
import com.dj.mall.common.ResultModel;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Order;
import com.dj.mall.domain.mall.entiy.Reply;
import com.dj.mall.domain.mall.entiy.ShoppingCar;
import com.dj.mall.domain.mall.vo.OrderVo;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import com.dj.mall.util.AliPayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    /**
     * 确认订单  提交订单  保存 订单支付方式 收货地址  修改 订单状态 已支付
     * @return   idsInteger 购物车ids
     */
    @RequestMapping("sureOrder")
    public String sureOrder(Integer [] ids, String address, String payStatus,  String token){
        try {
            //根据勾选购物车  确定订单的ids 展示到确认订单页面   同时将ids里的数据添加到Order表里
            //生成父级订单号
            Random random = new Random();
            String result="";
            for(int i=0;i<3;i++){
                result+=random.nextInt(SystemConstant.TEN); //10
            }
            Date date = new Date();
            Format f = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format = f.format(date);
            String pNum = "Dj"+format+result;
            List<ShoppingCar> shoppingCarByIds = userService.findShoppingCarByIds(ids);
            //组装数据 组装orderList去批量新增
            List<Order> orderList = new ArrayList<>();
            double tolal = SystemConstant.NUMBER_ZERO;
            String nameShow = "";
            for (ShoppingCar shoppingCar : shoppingCarByIds) {
                Order order = new Order();
                //地址  收货信息
                order.setAddress(address);
                order.setPayStatus(payStatus);
                order.setUserId(shoppingCar.getUserId());
                order.setFreightMoney(shoppingCar.getProFreight());
                order.setProductName(shoppingCar.getProductName());
                order.setProSkuId(shoppingCar.getProSkuId());
                //根据skuid  查询商品id   根据商品id   查询卖家id  保存到订单表  目的不同卖家展示自己的订单
                ProductSku productSku = userService.findProductSkuById(shoppingCar.getProSkuId());
                //根据productSku 中productId查询 product表中的卖家userId
                Product product = userService.getProductById(productSku.getProductId());
                //赋值商品对应的卖家
                order.setProductId(productSku.getProductId());
                order.setSellerId(product.getUserId());
                order.setProNum(shoppingCar.getNum());
                order.setOriginalPrice(shoppingCar.getOldPrice());
                order.setActualPrice(shoppingCar.getNewPrice());
                order.setRate(shoppingCar.getProRate());
                order.setSkuAttrName(shoppingCar.getProSku());
                //1表示待付款
                order.setOrderSonStatus(SystemConstant.NUMBER_SON_NUM_STATUS_ONE);
                order.setDeliveryTime(new Date());
                order.setParentNum(pNum);
                //生成子集订单
                Random random1 = new Random();
                String result1="";
                for(int i=0;i<3;i++){
                    result1+=random1.nextInt(SystemConstant.NUMBER_TEN); //10
                }
                Date date1 = new Date();
                Format f1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String format1 = f1.format(date1);
                String sonNum = "Dj"+format1+result1;
                order.setOrderSonNum(sonNum);
                orderList.add(order);

                tolal += shoppingCar.getNewPrice()*shoppingCar.getNum() + shoppingCar.getProFreight();
                nameShow += shoppingCar.getProductName();
            }
            //添加订单 待支付 1   删除购物车  减库存
            userService.insertOrder(orderList, ids);
            //支付宝 支付 成功则状态2 已支付  否则待支付
            return AliPayUtils.toAliPay(pNum,tolal,nameShow,token);
        } catch (Exception e) {
            e.printStackTrace();
            return "异常";
        }
    }






    /**
     * 立即购买  查看库存计算
     * @param proSkuId  商品skuid
     * @param num  数量
     * @return
     */
    @RequestMapping("buyNowCheckCount")
    public ResultModel buyNowCheckCount(Integer proSkuId, Integer num){
        try {
            //根据sku  id  对应的库存 和前台传来的库存进行比较
            ProductSku sku = userService.findProductSkuById(proSkuId);
            //判断前台传来的库存   和sku表里库存进行比较
            //num 大于 库存 显示最大库存数
            if(sku.getSkuCount() == SystemConstant.NUMBER_ZERO){ // 0
                return new ResultModel().error("无货");
            }
            if(num > sku.getSkuCount()){
                return new ResultModel().error("库存不足， 库存仅剩"+sku.getSkuCount()+"件");
            }
            return new ResultModel().success("有货");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }




    /**
     * 立即购买  将数据回显  然后展示
     * @param orderVo
     * @return
     */
   /* @RequestMapping("buyNow")
    public ResultModel buyNow(OrderVo orderVo, String token, Integer num,Integer proSkuId){

        Map<String, Object> map = new HashMap<>();
        try {
            //根据sku  id  对应的库存 和前台传来的库存进行比较
            ProductSku sku = userService.findProductSkuById(proSkuId);
            //判断前台传来的库存   和sku表里库存进行比较
            //num 大于 库存 显示最大库存数
            if(sku.getSkuCount() == SystemConstant.NUMBER_ZERO){ // 0
                return new ResultModel().error("无货");
            }
            if(num > sku.getSkuCount()){
                return new ResultModel().error("库存不足， 库存仅剩"+sku.getSkuCount()+"件");
            }
            orderVo.setProNum(num);

            Order order = new Order();
            BeanUtils.copyProperties(orderVo, order);
            //最新价格的计算   原价 折扣 原价*折扣
            if(order.getRate() == SystemConstant.NUMBER_ZERO){
                order.setActualPrice(order.getOriginalPrice());
            } else {
                order.setActualPrice( order.getOriginalPrice()*order.getRate()/SystemConstant.NUMBER_ONE_HOUNDER );
            }
            //生成父级订单号
            Random random = new Random();
            String result="";
            for(int i=0;i<3;i++){
                result+=random.nextInt(SystemConstant.NUMBER_TEN);
            }
            Date date = new Date();
            Format f = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format = f.format(date);
            String pNum = "Dj"+format+result;
            //生成子集订单
            Random random1 = new Random();
            String result1="";
            for(int i=0;i<3;i++){
                result1+=random1.nextInt(SystemConstant.NUMBER_TEN);
            }
            Date date1 = new Date();
            Format f1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format1 = f1.format(date1);
            String sonNum = "Dj"+format1+result1;

            order.setParentNum(pNum);
            order.setOrderSonNum(sonNum);
            order.setDeliveryTime(new Date());
            order.setSkuAttrName(sku.getSkuAttrValueNames());
            //根据productSku 中productId查询 product表中的卖家userId
            Product product = userService.getProductById(sku.getProductId());
            //赋值卖家的id、
            order.setSellerId(product.getUserId());
            order.setProductId(sku.getProductId());
            //保存
            orderService.insertOrder(order,token);

            map.put("pNum",pNum);
            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }*/


    /**
     * 立即购买  将数据回显  然后展示
     * @param orderVo
     * @return
     */
    @RequestMapping("buyNow")
    public String buyNow(OrderVo orderVo, String token, Integer num, Integer proSkuId){
        Map<String, Object> map = new HashMap<>();
        try {
            //根据sku  id  对应的库存 和前台传来的库存进行比较
            ProductSku sku = userService.findProductSkuById(proSkuId);
            //判断前台传来的库存   和sku表里库存进行比较
            //num 大于 库存 显示最大库存数
            if(sku.getSkuCount() == SystemConstant.NUMBER_ZERO){ // 0
                return "无货";
            }
            if(num > sku.getSkuCount()){
                return "库存不足， 库存仅剩"+sku.getSkuCount()+"件";
            }
            orderVo.setProNum(num);

            Order order = new Order();
            BeanUtils.copyProperties(orderVo, order);
            //最新价格的计算   原价 折扣 原价*折扣
            if(order.getRate() == SystemConstant.NUMBER_ZERO){
                order.setActualPrice(order.getOriginalPrice());
            } else {
                order.setActualPrice( order.getOriginalPrice()*order.getRate()/SystemConstant.NUMBER_ONE_HOUNDER );
            }
            //生成父级订单号
            Random random = new Random();
            String result="";
            for(int i=0;i<3;i++){
                result+=random.nextInt(SystemConstant.NUMBER_TEN);
            }
            Date date = new Date();
            Format f = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format = f.format(date);
            String pNum = "Dj"+format+result;
            //生成子集订单
            Random random1 = new Random();
            String result1="";
            for(int i=0;i<3;i++){
                result1+=random1.nextInt(SystemConstant.NUMBER_TEN);
            }
            Date date1 = new Date();
            Format f1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format1 = f1.format(date1);
            String sonNum = "Dj"+format1+result1;

            order.setParentNum(pNum);
            order.setOrderSonNum(sonNum);
            order.setDeliveryTime(new Date());
            order.setSkuAttrName(sku.getSkuAttrValueNames());
            //根据productSku 中productId查询 product表中的卖家userId
            Product product = userService.getProductById(sku.getProductId());
            //赋值卖家的id、
            order.setSellerId(product.getUserId());
            order.setProductId(sku.getProductId());
            //保存
            orderService.insertOrder(order,token);
            //应付金额  数量* 实际价格  + 邮费
            double tolal =  order.getActualPrice()*order.getProNum() + order.getFreightMoney();

            /*map.put("pNum",pNum);
            return new ResultModel().success(map);*/
            return AliPayUtils.toAliPay(pNum,tolal,order.getProductName()+order.getSkuAttrName(),token);
        } catch (Exception e) {
            e.printStackTrace();
            return "异常";
        }
    }

    /**
     * 异步调用  支付成功 回调修改状态
     * @param request
     * @return
     * @throws AlipayApiException
     */
    @RequestMapping("aliPayCallBack")
    public String aliPayCallBack(HttpServletRequest request) throws AlipayApiException {
        System.out.println(request);
        Map<String, String> map = AliPayUtils.aliPayCallBack(request);
        try {
            if(null == map || map.isEmpty()){
                return "error";
            }
            // 1:获取到你传入支付宝的订单号
            String pOrderNumber = map.get("merchant_order_number");
            String aliPayNum = map.get("alipay_transaction_serial_number");
            /** 待支付,待发货,待收货,已完成,已取消 */
            // 2:修改订单状态 为 待发货
            if(pOrderNumber.contains("only-")){  // only-12121212112
                String[] orderNumArr = pOrderNumber.split("-");
               /* Order order = orderService.findOrderByOrderNum(orderNumArr[1]);*/
                /**
                 * 1: 服务器返回success，因为网络原因支付宝没有接收到
                 * 2：支付宝会再次调用进行更改订单状态
                 * 3：如果这时订单状态已经更改为待收货
                 * 4：支付宝会再次调用时如果不做判断会将该订单重新修改为  待发货
                 * 5：造成脏数据
                 */
               /* if(order.getOrderStatus().equals("0")){
                    order.setOrderStatus("1");
                    order.setAlipayNum(aliPayNum);
                    orderService.update(order);
                }*/
                return "success";
            }
            List<Order> orderList = orderService.findListByPNum(pOrderNumber);
            for (Order order : orderList) {
                if(order.getOrderSonStatus() == SystemConstant.ONE){  //1
                    order.setAlipayNum(aliPayNum);
                    order.setOrderSonStatus(SystemConstant.TWO); //2
                    order.setMessage(SystemConstant.ONE);   //1
                    order.setPayTime(new Date());
                    order.setMessageTime(new Date());
                    orderService.updateOrderAlipayById(order);
                }
            }

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "异常";
        }
    }





    /**
     * 去展示待付款
     * @return
     */
    @RequestMapping("waitPayList")
    public ResultModel waitPayList(Integer pageNo, String token){
            HashMap<String, Object> map = new HashMap<>();
        try {
            Page<Order> page = new Page<>(pageNo, SystemConstant.PAGESIZE_NUMBER);
            Page<Order> orderPage = orderService.findOrderByParentNum(page, token);
            map.put("list",orderPage.getRecords());
            map.put("pageNo",orderPage.getCurrent());
            map.put("totalPage",orderPage.getPages());

            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }


    /**
     * 去展示已支付 已完成 已拒绝的
     * @return
     */
    @RequestMapping("orderStatus")
    public ResultModel orderStatus(Integer pageNo, String token,Integer orderSonStatus){
        HashMap<String, Object> map = new HashMap<>();
        try {
            Page<Order> page = new Page<>(pageNo, SystemConstant.PAGESIZE_NUMBER);
            Page<Order> order = orderService.updateFindOrderBySonStatus(page, token, orderSonStatus);
            map.put("list",order.getRecords());
            map.put("pageNo",order.getCurrent());
            map.put("totalPage",order.getPages());
            System.out.println(order.getPages());
            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 待付款 页面    去支付
     * @param order  参数   订单状态  已支付  message 1 待发货 messageTime   发货时间   付款时间
     * @return
     */
    @RequestMapping("payOrder")
    public String payOrder(Order order, String token){
        try {
            order.setMessageTime(new Date());
            order.setPayTime(new Date());
            double total = SystemConstant.NUMBER_ZERO;
            String nameShow = "";
            List<Order> orderList = orderService.getOrderByPnum(order.getParentNum());
            for (Order order1 : orderList) {
                order1.setPayTime(order.getPayTime());
                order1.setOrderSonStatus(order.getOrderSonStatus());
                order1.setMessage(order.getMessage());
                order1.setMessageTime(new Date());
                order1.setAddress(order.getAddress());
                order1.setPayStatus(order.getPayStatus());
                total += order1.getActualPrice()* order1.getProNum() + order1.getFreightMoney();
                nameShow += order1.getProductName();
            }
           /* orderService.updateOrderByList(orderList);*/
            //调用工具类  订单号 商品名  价格
            return AliPayUtils.toAliPay(order.getParentNum(),total,nameShow, token);
        } catch (Exception e) {
            e.printStackTrace();
            return "异常";
        }
    }



    /**
     * 取消订单  修改状态  变成已取消  已取消 状态4
     * @param parentNum
     * @return
     */
    @RequestMapping("delOrder")
    public ResultModel delOrder(String parentNum){
        try {
            orderService.deleteOrderByParentNum(parentNum);
            return new ResultModel().success("取消成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }




    /**
     * 再次购买  根据order id  或者 子集订单号 修改状态  为 已支付 收货地址  支付方式
     * @param   order 根据id 修改  实体类传参
     * @return
     */
    @RequestMapping("buyAgain")
    public ResultModel buyAgain(Order order){
        try {
            orderService.updateOrderById(order);
            return new ResultModel().success("支付成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }


    /**
     * 提醒发货  将状态1 变为         2 已提醒
     * @param orderSonNum  子集 订单
     * @return
     */
    @RequestMapping("fahuoMsg")
    public ResultModel fahuoMsg(String orderSonNum, Integer message){
        try {
            orderService.updateMessage(orderSonNum, message);
            return new ResultModel().success("提醒发货成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }



    /**
     * 确认收货   将子集订单 改为3  已完成
     * @param orderSonNum  子集 订单
     * @return
     */
    @RequestMapping("sureShouhuo")
    public ResultModel sureShouhuo(String orderSonNum, Integer orderSonStatus){
        try {
            orderService.updateSonNumStatus(orderSonNum, orderSonStatus);
            return new ResultModel().success("成功收货");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }













    /**
     * 去保存买家评论
     * @param score
     * @param
     * @return
     */
    @RequestMapping("insertCommon")
    public ResultModel insertCommon(String orderSonNum, Integer score, String context,String token){
        try {
            //声明实体类
            Common common = new Common();
            Order order = orderService.getOrderBySonNum(orderSonNum);
            common.setProductId(order.getProductId());
            common.setContext(context);
            common.setUserId(order.getUserId());
            common.setCreateTime(new Date());
            common.setScore(score);
            orderService.insertCommon(common);
            return new ResultModel().success("评论成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }


    /**
     * 评论展示页面    //页面传来1 2 3 4
     *  1 所有评论  2 好评  3 中评 4 差评
     * @return
     */
    @RequestMapping("commonList")
    public ResultModel commonList(Common common, Integer pageNo){
        Map<String, Object> map = new HashMap<>();
        try {
            Page<Common> page = new Page<>(pageNo, SystemConstant.PAGESIZE_NUMBER);
            Page<Common> pageInfo = orderService.findCommonByProductId(page, common);
            List<Common> commonList = pageInfo.getRecords();

            //查询评论 根据product id 查询
            for (Common common1 : commonList) {
                //字符串表示 时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                common1.setCreateTimeShow(df.format(common1.getCreateTime()));
                List<Reply> replyList = common1.getReplyList();
                for (Reply reply : replyList) {
                    reply.setCreateTimeShow(df.format(reply.getCreateTime()));
                }
            }
            map.put("list", commonList);
            map.put("pageNo", pageInfo.getCurrent());
            map.put("totalPage", pageInfo.getPages());

            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }



    /**
     * 买家去回复    评论id  回复内容               回复类型  1
     * @return
     */
    @RequestMapping("reply")
    public ResultModel reply(Reply reply){
        try {
            reply.setCreateTime(new Date());
            //保存
            orderService.insertReply(reply);
            return new ResultModel().success("回复成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }



}
