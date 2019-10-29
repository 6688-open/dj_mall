package com.dj.mall.domain.basedata.dictionary.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DictionaryVo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String code;

    private String dictionaryName;

    private String parentCode;
}
