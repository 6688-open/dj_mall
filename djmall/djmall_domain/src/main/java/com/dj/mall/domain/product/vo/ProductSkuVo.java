package com.dj.mall.domain.product.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ProductSkuVo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 价格
     */
    private Integer skuPrice;
    /**
     * 库存
     */
    private Integer skuCount;
    /**
     * 折扣
     */
    private Integer skuRate;
    /**
     * 状态
     */
    private Integer skuStatus;
    /**
     * 属性名Id 字符串 1，2，3，4
     */
    private String  skuAttrIds;
    /**
     * 属性名  字符串 1，2，3，4
     */
    private String  skuAttrNames;
    /**
     * 属性值ID 字符串 1，2，3，4
     */
    private String  skuAttrValueIds;
    /**
     * 属性值 字符串 1，2，3，4
     */
    private String  skuAttrValueNames;
    /**
     * 是否默认
     */
    private String  isDefault;


















}
