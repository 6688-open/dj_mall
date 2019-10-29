package com.dj.mall.domain.basedata.sku.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("djmall_product_sku_gm")
@Accessors(chain = true)
public class ProductSkuGm implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 商品类型
     */
    private String productType;
    /**
     * 属性id
     */
    private Integer attrId;

    /**
     * 展示属性名
     */
    @TableField(exist = false)
    private String attrShow;
    /**
     * 属性值
     */
    @TableField(exist = false)
    private String  attrValueShow;
    @TableField(exist = false)
    private String [] attrValueShowArr;
    /**
     * 属性值id
     */
    @TableField(exist = false)
    private String  attrValueId;
    @TableField(exist = false)
    private String []attrValueIdArr;










    /**
     * code
     */
    @TableField(exist = false)
    private String codeShow;
}
