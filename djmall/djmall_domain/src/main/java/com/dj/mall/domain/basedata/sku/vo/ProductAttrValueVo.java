package com.dj.mall.domain.basedata.sku.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ProductAttrValueVo implements Serializable {

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

}
