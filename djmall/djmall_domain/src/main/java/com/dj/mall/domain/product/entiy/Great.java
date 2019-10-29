package com.dj.mall.domain.product.entiy;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("dj_mall_great")
@Accessors(chain = true)
public class Great implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     *  商品sku
     */
    private Integer proSkuId;
    /**
     * userId
     */
    private Integer userId;







}
