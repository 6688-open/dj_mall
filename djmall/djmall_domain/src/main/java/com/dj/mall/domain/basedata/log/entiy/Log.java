package com.dj.mall.domain.basedata.log.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("log")
@Accessors(chain = true)
public class Log implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String dicCode;

    private String type;
    @TableField(exist = false)
    private String  freightShow;
}
