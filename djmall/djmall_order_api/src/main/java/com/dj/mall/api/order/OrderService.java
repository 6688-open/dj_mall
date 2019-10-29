package com.dj.mall.api.order;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Order;
import com.dj.mall.domain.mall.entiy.Reply;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface OrderService /*extends IService<User>*/ {
    /**
     * 确认订单  将待支付的 订单状态修改为已支付  收货地址   支付方式
     * @return  返回List集合
     * @throws Exception
     */
    void updateOrderToOrderSure(String address, String payStatus, String pNum, String token, Integer[] ids) throws Exception;

    /**
     * 添加订单  立即购买  状态是待支付
     * @param order 传递参数
     * @param token 添加用户Id  从redis获取
     * @throws Exception
     */
    void insertOrder(Order order, String token)throws Exception;

    /**
     * 根据id 去查询Order 一条数据
     * @return
     * @throws Exception
     */
    Order findOrderidById(Integer id)throws Exception;
    /**
     * pNum 去查询Order 一条数据
     * @return
     * @throws Exception
     */
    Order findOrderidByPNum(String pNum)throws Exception;

    /**
     * 根据父级订单查询   多个  一个
     * @param pNum
     * @return
     * @throws Exception
     */
    List<Order> findListByPNum(String pNum)throws Exception;

    /**
     * 展示待支付的订单  根据父级订单查询
     * @param page 状态是 待支付的  登陆人的Id
     * @return 返回page  分页
     * @throws Exception
     */
    Page<Order> findOrderByParentNum(Page page, String token)throws Exception;
    /**
     * 展示已支付  已完成  已拒绝的订单 ySonStatus 订单查询
     * @param page
     * @param token
     * @param orderSonStatus  状态
     * @return
     * @throws Exception
     */
    Page<Order> updateFindOrderBySonStatus(Page page, String token, Integer orderSonStatus)throws Exception;

    /**
     * 根据父级订单查询集合
     * @param Pnum
     * @return
     * @throws Exception
     */
    List<Order> getOrderByPnum(String Pnum)throws Exception;

    /**
     * 批量修改  将状态修改为已支付
     * @param orderList
     * @throws Exception
     */
    void updateOrderByList(List<Order> orderList)throws Exception;
    /**
     * 展示父级订单的信息  查询本人登录的订单 userId
     * @param parentNum  根据parentNum查询
     * @param token   userId条件
     * @return
     * @throws Exception
     */
    List<Order> getOrderByParentNumAndUserId(String parentNum, String token)throws Exception;
    /**
     * 展示子级订单的信息
     * @param sonNum  根据SonNum查询
     * @return  返回一条数据
     * @throws Exception
     */
    Order getOrderBySonNum(String sonNum)throws Exception;

    /**
     * 根据 ParentNum    取消订单  修改状态  变成已取消  已取消 状态4
     * @param parentNum  根据父级num单号  删除
     * @throws Exception
     */
    void deleteOrderByParentNum(String parentNum)throws Exception;

    /**
     * 再次购买  根据order id  或者 子集订单号orderSonNum 修改状态  为 已支付 收货地址  支付方式
     * @param   order 根据id 修改  实体类传参
     */
    void updateOrderById(Order order)throws Exception;

    /**
     * 付款成功  修改状态
     * @param order
     * @throws Exception
     */
    void updateOrderAlipayById(Order order)throws Exception;
    /**
     * 提醒发货  将状态1 变为         2 已提醒
     * @param orderSonNum  子集 订单
     * @return
     */
    void updateMessage(String orderSonNum, Integer message)throws Exception;

    /**
     * 确认收货   将子集订单 改为3  已完成
     * @param orderSonNum  子集订单
     * @param orderSonStatus  3
     * @throws Exception
     */
    void updateSonNumStatus(String orderSonNum, Integer orderSonStatus)throws Exception;













    /**
     * 晒单评价  保存到评论表
     * @param common
     * @throws Exception
     */
    void insertCommon(Common common)throws Exception;


    /**
     * 查询评论展示     指定商品 商品的所有评论
     * @param   common   根据商品id  查询
     * @return      返回集合
     * @throws Exception
     */
    Page<Common> findCommonByProductId(Page page, Common common)throws Exception;

    /**
     * 买家回复
     * @param reply
     * @throws DataAccessException
     */
    void insertReply(Reply reply)throws Exception;

















    /**
     * 45分钟 立即购买  加入购物车 确认订单 45 分钟  自动取消订单
     * @throws DataAccessException
     */
    void updateStatusAndCount()throws Exception;

    /**
     * 15 天  已发货 自动从待收货 变成已完成  已发货  子集订单状态2  message状态是3 已发货
     * @return
     * @throws Exception
     */
    void updateFindOrderBySonNumStatusAndMessage()throws Exception;

    /**
     * 待发货
     * 弹框消息提醒
     * @param userId
     * @param message
     * @return
     */
    List<Order> findOrderAdmin(Integer userId, Integer[] message, Integer orderSonStatus) throws Exception;
}
