package com.dj.mall.domain.mall.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("dj_mall_order")
public class Order implements Serializable {
    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 买家用户Id*/
    private Integer userId;
    /** 卖家用户Id*/
    private Integer sellerId;
    /** 父级订单*/
    private String parentNum;
    /** 子集订单 */
    private String orderSonNum;
    /** 商品id */
    private String productName;
    /** 商品skuId */
    private Integer proSkuId;
    /** 商品Id*/
    private Integer productId;
    /** 购买数量 */
    private Integer proNum;
    /** 运费金额*/
    private Integer freightMoney;
    /** 原价格 */
    private Integer originalPrice;
    /** 实际价格 */
    private Integer actualPrice;
    /** 折扣 */
    private Integer rate;
    /** sku属性名值 */
    private String skuAttrName;
    /** 子集订单状态 */
    private Integer orderSonStatus;
    /** 提醒发货: 0卖家发货 1买家提醒 */
    private Integer message;
    /** 提醒发货六小时内不能重复使用 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date messageTime;
    /** 评论 0不能再评论 1用户评论 2商家回复 */
    private Integer comments;
    /** 下单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;
    /** 下单时间字符串表示 */
    @TableField(exist = false)
    private String deliveryTimeShow;
    /** 付款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    /** 付款时间字符串表示 */
    @TableField(exist = false)
    private String payTimeShow;
    /** 取消订单时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cancelTime;
    /**
     * 支付宝单号
     */
    private String AlipayNum;
    /** 取消订单时间  字符串*/
    @TableField(exist = false)
    private Date cancelTimeShow;



    /**支付方式   1 货到付款  2 支付宝 3 微信*/
    private String payStatus;

    /** 收货人地址*/
    private String address;

    /*待支付展示*/
    /**
     * 商品个数  父级订单包含几个订单
     */
    @TableField(exist = false)
    private Integer number;
    /**
     * 总钱数  包含邮费（应付）
     */
    @TableField(exist = false)
    private Integer totalMoney;
    /**
     * 商品总钱数  不打折
     */
    @TableField(exist = false)
    private Integer allMoney;
    /**
     * 总邮费
     */
    @TableField(exist = false)
    private Integer totalFreight;
    /**
     * 父级订单包含几个订单的商品名
     */
    @TableField(exist = false)
    private String productNameShow;


    /**
     * 编号
     */
    @TableField(exist = false)
    private Integer bianhao;

    /**
     * 下单人姓名
     */
    @TableField(exist = false)
    private String username;
    /**
     * 下单人手机号码
     */
    @TableField(exist = false)
    private String phone;


    /**
     * 订单自动取消  45分钟设定时间
     */
    @TableField(exist = false)
    private Date redisTime;


}
