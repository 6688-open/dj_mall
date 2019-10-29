package com.dj.mall.domain.mall.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("shopping_car")
@Accessors(chain = true)
public class ShoppingCar implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 原价
     */
    private Integer oldPrice;
    /**
     * sku属性
     */
    private String proSku;
    /**
     * 折扣
     */
    private Integer proRate;
    /**
     * 运费
     */
    private Integer proFreight;
    /**
     * 最新价格
     */
    private Integer newPrice;
    /**
     * 商品sku id
     */
    private Integer proSkuId;
    /**
     * 商品数量
     */
    private Integer num;



}
