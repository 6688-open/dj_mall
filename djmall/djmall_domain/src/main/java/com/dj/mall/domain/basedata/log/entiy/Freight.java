package com.dj.mall.domain.basedata.log.entiy;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("freight")
@Accessors(chain = true)
public class Freight implements Serializable {


    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer  logId;

    private String  freight;
    @TableField(exist = false)
    private String logName;
}
