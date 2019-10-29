package com.dj.mall.mapper.mall;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Order;
import com.dj.mall.domain.mall.entiy.Reply;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

public interface OrderMapper extends BaseMapper<Order> {

    /**
     *  当提交订单 ProductSku对应的库存数 要减去 购买的数量
     * @param id
     * @param num
     * @throws DataAccessException
     */
    void updateProductSkuById(@Param("id") Integer id, @Param("num") Integer num) throws DataAccessException;

    /**
     *  当提交订单 ProductSku对应的库存数 回填库存
     * @param id
     * @param num
     * @throws DataAccessException
     */
    void updateProductSkuByIdAddCount(@Param("id") Integer id, @Param("num") Integer num) throws DataAccessException;

    /**
     * 展示待支付的订单  根据父级订单查询
     * @param page
     * @param userId  状态是 待支付的  登陆人的Id
     * @return 返回page  分页
     * @throws DataAccessException
     */
    Page<Order> findOrderByParentNum(Page page, @Param("userId") Integer userId)throws DataAccessException;

    /**
     * 展示已支付  已完成  已拒绝的订单 ySonStatus 订单查询
     * @param page
     * @param userId
     * @param orderSonStatus  状态
     * @return
     * @throws DataAccessException
     */
    Page<Order> findOrderBySonStatus(Page page, @Param("userId") Integer userId,@Param("orderSonStatus") Integer orderSonStatus)throws DataAccessException;














    /**
     * 晒单评价  保存到评论表
     * @param common
     * @throws DataAccessException
     */
    void insertCommon(Common common)throws DataAccessException;



    /**
     * 查询评论展示     指定商品 商品的所有评论
     * @param common    根据商品id  查询
     * @return      返回集合
     * @throws DataAccessException
     */
    Page<Common> findCommonByProductId(Page page, @Param("common") Common common)throws DataAccessException;


    /**
     * 买家回复
     * @param reply
     * @throws DataAccessException
     */
    void insertReply(Reply reply)throws DataAccessException;

}
