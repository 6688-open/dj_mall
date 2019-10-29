package com.dj.mall.domain.mall.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain=true)
public class AreaVo implements Serializable {


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