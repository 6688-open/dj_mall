package com.dj.mall.domain.basedata.dictionary.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("dictionary")
@Accessors(chain = true)
public class Dictionary implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String code;

    private String dictionaryName;

    private String parentCode;
}
