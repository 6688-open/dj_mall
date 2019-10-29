package com.dj.mall.domain.basedata.sku.entiy;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("djmall_product_attr_value")
@Accessors(chain = true)
public class ProductAttrValue implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 属性id
     */
    private Integer attrId;
    /**
     * 属性值
     */
    private String attrValue;
    /**
     * 属性值
     */
    @TableField(exist = false)
    private  String atVaShow;

}
