package com.dj.mall.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.mall.api.order.OrderService;
import com.dj.mall.api.order.cmpt.RedisService;
import com.dj.mall.api.order.user.UserService;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Order;
import com.dj.mall.domain.mall.entiy.Reply;
import com.dj.mall.domain.user.entiy.User;
import com.dj.mall.mapper.mall.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    /**
     * 确认订单  将待支付的 订单状态修改为已支付  收货地址   支付方式
     *
     * 同时将传来的ids  删除购物车里的要提交订单的数据
     * @return  返回List集合
     * @throws Exception
     */
    @Override
    public void updateOrderToOrderSure(String address, String payStatus, String pNum, String token, Integer[] ids) throws Exception {
        //获取登陆人的Id redis 获取
        User user = redisService.get(token);
        // 根据pNum查询订单的id  进行批量修改
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_num",pNum);
        List<Order> orderList = this.list(queryWrapper);
        for (Order order : orderList) {
            order.setPayStatus(payStatus);
            order.setAddress(address);
            //将待支付改成已支付
            order.setOrderSonStatus(SystemConstant.NUMBER_SON_NUM_STATUS_TWO);
            //将message 改成  1  提醒卖家发货   messgae_time 当前时间
            order.setMessage(SystemConstant.NUMBER_ONE);
            order.setMessageTime( new Date());
            order.setUserId(user.getId());
            order.setPayTime(new Date());

            //支付成功 对应sku表里的库存减少   order里有sku 的id  和数量num
            orderMapper.updateProductSkuById(order.getProSkuId(),order.getProNum());
        }
        //提交订单  将待支付的 订单状态修改为已支付  收货地址   支付方式
        this.updateBatchById(orderList);
        //删除购物车里的要提交订单的数据    共用的购物车里的批量删除  在userController
        if(ids != null){
            userService.deleteShoppingCarByIds(ids);
        }


    }
    /**
     * 添加订单  立即购买  状态是待支付
     * @param order 传递参数
     * @param token 添加用户Id  从redis获取
     * @throws Exception
     */
    @Override
    public void insertOrder(Order order, String token) throws Exception {

        //将确认订单加入redis   查询redis 45分钟自动取消订单
        //存子集订单号  作为第二个key  i 对象
        //有效时间设置  45分钟
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, SystemConstant.NUMBER_FOUR_FIVE);// 今天+45 分钟
        Date date = c.getTime();
        order.setRedisTime(date);
        redisService.pushHash("ORDER", order.getOrderSonNum(),order);


        User user = redisService.get(token);
        order.setUserId(user.getId());
        //保存 订单
        this.save(order);
        //减去库存
        orderMapper.updateProductSkuById(order.getProSkuId(), order.getProNum());

    }
    /**
     * 根据id 去查询Order 一条数据
     * @return
     * @throws Exception
     */
    @Override
    public Order findOrderidById(Integer id) throws Exception {
        return this.getById(id);
    }
    /**
     * pNum 去查询Order 一条数据
     * @return
     * @throws Exception
     */
    @Override
    public Order findOrderidByPNum(String pNum) throws Exception {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_num", pNum);
        return this.getOne(queryWrapper);
    }
    /**
     * 根据父级订单查询   多个  一个
     * @param pNum
     * @return
     * @throws Exception
     */
    @Override
    public List<Order> findListByPNum(String pNum) throws Exception {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_num", pNum);
        return this.list(queryWrapper);
    }

    /**
     * 展示待支付的订单  根据父级订单查询
     * @param page
     * @param token  状态是 待支付的  登陆人的Id
     * @return 返回page  分页
     * @throws Exception
     */
    @Override
    public Page<Order> findOrderByParentNum(Page page, String token) throws Exception {
        User user = redisService.get(token);
        return orderMapper.findOrderByParentNum(page,user.getId());
    }
    /**
     * 展示已支付  已完成  已拒绝的订单 ySonStatus 订单查询
     * @param page
     * @param token
     * @param orderSonStatus  状态
     * @return
     * @throws Exception
     */
    @Override
    public Page<Order> updateFindOrderBySonStatus(Page page, String token, Integer orderSonStatus) throws Exception {
        //判断当前时间  和提醒时间是否在6小时之内  否则不可以再次提醒  是已提醒状态
        // 如果超过6个小时 修改为状态1  变为提醒发货状态
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("message",SystemConstant.STATUS_NUMBER_TWO); // 2已提醒
        List<Order> list = this.list(queryWrapper);
        for (Order order : list) {
            if(new Date().getTime() > order.getMessageTime().getTime() && order.getMessage() == SystemConstant.STATUS_NUMBER_TWO){
                order.setMessage(SystemConstant.NUMBER_ONE); //1 提醒卖家发货
                this.updateById(order);
            }
        }
        User user = redisService.get(token);
        return orderMapper.findOrderBySonStatus(page, user.getId(),orderSonStatus);
    }

    /**
     * 根据父级订单查询集合
     * @param Pnum
     * @return
     * @throws Exception
     */
    @Override
    public List<Order> getOrderByPnum(String Pnum) throws Exception {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_num", Pnum);
        return this.list(queryWrapper);
    }
    /**
     * 批量修改  将状态修改为已支付
     * @param orderList
     * @throws Exception
     */
    @Override
    public void updateOrderByList(List<Order> orderList) throws Exception {
        this.updateBatchById(orderList);
    }

    /**
     * 展示父级订单的信息  查询本人登录的订单 userId
     * @param parentNum  根据parentNum查询
     * @param token   userId条件
     * @return
     * @throws Exception
     */
    @Override
    public List<Order> getOrderByParentNumAndUserId(String parentNum, String token) throws Exception {
        //获取登陆人的Id redis 获取
        User user = redisService.get(token);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_num",parentNum);
        queryWrapper.eq("user_id",user.getId());
        return this.list(queryWrapper);
    }
    /**
     * 展示子级订单的信息
     * @param sonNum  根据SonNum查询
     * @return  返回一条数据
     * @throws Exception
     */
    @Override
    public Order getOrderBySonNum(String sonNum) throws Exception {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_son_num",sonNum);
        return this.getOne(queryWrapper);
    }
    /**
     * 根据 ParentNum 取消订单  修改状态  变成已取消  已取消 状态4   将库存加回来
     * @param parentNum  根据父级num单号  删除
     * @throws Exception
     */
    @Override
    public void deleteOrderByParentNum(String parentNum) throws Exception {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_num",parentNum);
        //根据父级订单 查询所有订单号  修改状态为 已取消的  4  取消时间 new Date
        List<Order> orderList = this.list(queryWrapper);
        for (Order order : orderList) {
            order.setOrderSonStatus(SystemConstant.NUMBER_SON_NUM_STATUS_FOUR);
            order.setCancelTime(new Date());
            //修改库存 将取消订单的库存还原
            orderMapper.updateProductSkuById(order.getProSkuId(), -order.getProNum());
        }
        this.updateBatchById(orderList);

    }
    /**
     * 再次购买  根据order id  或者 子集订单号orderSonNum 修改状态  为 已支付 收货地址  支付方式
     * @param   order 根据id 修改  实体类传参
     */
    @Override
    public void updateOrderById(Order order) throws Exception {
        //支付成功 对应sku表里的库存减少   order里有sku 的id  和数量num
        Order order1 = this.getById(order.getId());
        orderMapper.updateProductSkuById(order1.getProSkuId(),order1.getProNum());
        order.setPayTime(new Date());
        this.updateById(order);
    }
    /**
     * 付款成功  修改状态
     * @param order
     * @throws Exception
     */
    @Override
    public void updateOrderAlipayById(Order order) throws Exception {
        this.updateById(order);
    }

    /**
     * 提醒发货  将状态1 变为         2 已提醒
     * @param orderSonNum  子集 订单
     * @return
     */
    @Override
    public void updateMessage(String orderSonNum, Integer message) throws Exception {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, SystemConstant.NUMBER_THREE_SIX_ZERO);// 今天+360 分钟   3600
        Date date = c.getTime();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_son_num",orderSonNum);
        Order order = this.getOne(queryWrapper);
        //将message 状态改为 2 已提醒
        order.setMessage(message);
        order.setMessageTime(date);
        //有效时间设置

        //修改
        this.updateById(order);
    }
    /**
     * 确认收货   将子集订单 改为3  已完成
     * @param orderSonNum  子集订单
     * @param orderSonStatus  3
     * @throws Exception
     */
    @Override
    public void updateSonNumStatus(String orderSonNum, Integer orderSonStatus) throws Exception {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_son_num",orderSonNum);
        Order order = this.getOne(queryWrapper);
        order.setOrderSonStatus(orderSonStatus);
        //修改
        this.updateById(order);
    }











    /**
     * 晒单评价  保存到评论表
     * @param common
     * @throws DataAccessException
     */
    @Override
    public void insertCommon(Common common) throws Exception {
        orderMapper.insertCommon(common);
    }


    /**
     * 查询评论展示     指定商品 商品的所有评论
     * @param   common   根据商品id  查询
     * @return      返回集合
     * @throws Exception
     */
    @Override
    public Page<Common> findCommonByProductId(Page page, Common common) throws Exception {
        return orderMapper.findCommonByProductId(page, common);
    }

    /**
     * 买家回复
     * @param reply
     * @throws DataAccessException
     */
    @Override
    public void insertReply(Reply reply) throws Exception {
        orderMapper.insertReply(reply);
    }













    /**
     * 45分钟 立即购买  加入购物车 确认订单 45 分钟  自动取消订单
     * @throws DataAccessException
     */
    @Override
    public void updateStatusAndCount() throws Exception {
        //redis获取之前存入的订单
        List<Order> orderList = redisService.getHashValues("ORDER");
        for (Order order : orderList) {
            if(new Date().getTime() > order.getRedisTime().getTime()){
                //将状态改为 已取消  并且 回填库存
                //根据子集订单查询 一条数据  修改状态为4 已取消
                Order order1 = this.getOrderBySonNum(order.getOrderSonNum());
                order1.setOrderSonStatus(SystemConstant.NUMBER_SON_NUM_STATUS_FOUR);
                order1.setCancelTime(new Date());

                //回填库存
                orderMapper.updateProductSkuByIdAddCount(order1.getProSkuId(),order1.getProNum()*SystemConstant.STATUS_NUMBER_TWO);

                //修改
                this.updateOrderById(order1);

                //删除redis中的数据
                redisService.delHash("ORDER",order.getOrderSonNum());
            }
        }

    }

    /**
     * 15 天  已发货 自动从待收货 变成已完成  已发货  子集订单状态2  message状态是3 已发货
     * @return
     * @throws Exception
     */
    @Override
    public void updateFindOrderBySonNumStatusAndMessage() throws Exception {
        //查询数据库  支付完成 已发货的所有订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_son_status", SystemConstant.NUMBER_SON_NUM_STATUS_TWO); //2  支付完成
        queryWrapper.eq("message", SystemConstant.NUMBER_THREE);//3 已发货
        List<Order> list = this.list(queryWrapper);
        //遍历 发货时间超过15 天自动修改为已完成

        for (Order order : list) {
            //有效时间设置
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH,-SystemConstant.NUMBER_FIFTTER);// 当前时间 - 15天
            Date date = c.getTime(); // 发货时间 ，《 当前时间-15 天 自动改为已完成
            if( order.getMessageTime().getTime() < date.getTime() ){
                //修改订单状态  已完成 3
                order.setOrderSonStatus(SystemConstant.NUMBER_SON_NUM_STATUS_THREE);  //已完成
                //批量修改已完成
                this.updateBatchById(list);
            }
        }
    }


    /**
     * 待发货
     * 弹框消息提醒
     * @param userId
     * @param message
     * @return
     */
    @Override
    public List<Order> findOrderAdmin(Integer userId, Integer[] message, Integer orderSonStatus) throws Exception {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("seller_id", userId);//卖家ID
        wrapper.in("message", Arrays.asList(message));
        wrapper.eq("order_son_status", orderSonStatus);
        return this.list(wrapper);
    }

}
