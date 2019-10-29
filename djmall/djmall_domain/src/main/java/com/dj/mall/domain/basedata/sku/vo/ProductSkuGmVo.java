package com.dj.mall.domain.basedata.sku.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ProductSkuGmVo implements Serializable {

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
     * code
     */
    @TableField(exist = false)
    private String codeShow;
}
