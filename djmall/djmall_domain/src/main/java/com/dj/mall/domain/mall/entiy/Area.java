package com.dj.mall.domain.mall.entiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("djmall_area")
@Accessors(chain=true)
public class Area implements Serializable {


    /**
     * id
     */
    private Integer id;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 区域拼音
     */
    private String areaPinyin;
    /**
     * 区域父ID
     */
    private Integer areaParentId;
}