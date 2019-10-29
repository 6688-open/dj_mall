package com.dj.mall.domain.code.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("djmall_code")
@Accessors(chain = true)
public class Code implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 类型  1  忘记密码 手机验证码
     */
    private Integer type;
    /**
     * 手机号码
     */
    private String phone;
    /**
     *  手机验证码
     */
    private String code;
    /**
     * 有效时间
     */
    @JsonFormat(timezone = "GMT-8", pattern = "yyyy-MM-dd HH;mm;ss")
    private Date time;
}
