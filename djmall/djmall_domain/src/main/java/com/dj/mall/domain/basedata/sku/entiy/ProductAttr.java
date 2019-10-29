package com.dj.mall.domain.basedata.sku.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("djmall_product_attr")
@Accessors(chain = true)
public class ProductAttr implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 属性值展示
     */
    @TableField(exist = false)
    private String attrValueShow;


}
