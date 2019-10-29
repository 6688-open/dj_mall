package com.dj.mall.domain.mall.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("reply")
@Accessors(chain=true)
public class Reply implements Serializable {

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     *评论id
     */
    private Integer commentId;
    /**
     * 创建时间
     */
    private Date createTime;
    @TableField(exist = false)
    private String  createTimeShow;
    /**
     * 1 代表 买家   2 卖家
     */
    private Integer isUser;
    /**
     * 回复内容
     */
    private String context;
}
