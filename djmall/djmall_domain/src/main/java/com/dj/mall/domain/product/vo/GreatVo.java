package com.dj.mall.domain.product.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GreatVo implements Serializable {

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
