package com.dj.mall.domain.product.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ProductVo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 产品名
     */
    private String productName;
    /**
     * 字典数据 商品类型
     */
    private String dictionaryCode;

    @TableField(exist = false)
    private String[] dictionaryCodeArr;
    /**
     * 状态 1 上架 2 下架
     */
    private Integer productStatus;
    /**
     * 运费id
     */
    private Integer freightId;
    /**
     * 图片
     */
    private String picture;
    /**
     * 产品描述
     */
    private String productDescribe;
    /**
     * 点赞量
     */
    private Integer thumbNumber;
    /**
     * 订单量
     */
    private Integer orderNumber;

    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户等级 1 普通用户
     *          2  管理员
     */
    private Integer userLever;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 字典数据物流名称的id
     */
    @TableField(exist = false)
    private Integer logId;
    /**
     * 运费
     */
    @TableField(exist = false)
    private Integer freight;
    /**
     * 物流名称
     */
    @TableField(exist = false)
    private String dictionaryName;














    /**
     * 价格
     */
    @TableField(exist = false)
    private Integer[] skuPrice;
    /**
     * 库存
     */
    @TableField(exist = false)
    private Integer[] skuCount;
    /**
     * 折扣
     */
    @TableField(exist = false)
    private Integer[] skuRate;
    /**
     * 状态
     */
    @TableField(exist = false)
    private Integer[] skuStatus;
    /**
     * 属性名Id 字符串 1，2，3，4
     */
    @TableField(exist = false)
    private String[]  skuAttrIds;
    /**
     * 属性名  字符串 1，2，3，4
     */
    @TableField(exist = false)
    private String[]  skuAttrNames;
    /**
     * 属性值ID 字符串 1，2，3，4
     */
    @TableField(exist = false)
    private String [] skuAttrValueIds;
    /**
     * 属性值 字符串 1，2，3，4
     */
    @TableField(exist = false)
    private String [] skuAttrValueNames;
    /**
     * 是否默认
     */
    @TableField(exist = false)
    private String[]  isDefault;








    /*商城展示相关*/
    /**
     * sku 价格
     */
    @TableField(exist = false)
    private Integer priceShow;
    /**
     * 库存
     */
    @TableField(exist = false)
    private Integer countShow;
    /**
     * 折扣
     */
    @TableField(exist = false)
    private Integer rateShow;
    /**
     * 商品分类
     */
    @TableField(exist = false)
    private String dictionaryNameShow;

    /**
     * 开始价格
     */
    @TableField(exist = false)
    private Integer startPrice;
    /**
     * 结束价格
     */
    @TableField(exist = false)
    private Integer endPrice;

    /**
     * sku  id
     */
    @TableField(exist = false)
    private Integer proSkuId;


}
