package com.dj.mall.domain.mall.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AddressVo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 收货人
     */
    private String addressName;

    /**
     * 省Id
     */
    private Integer proId;
    /**
     * 市id
     */
    private Integer cityId;
    /**
     * 区id
     */
    private Integer areaId;


}
