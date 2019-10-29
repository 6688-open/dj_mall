package com.dj.mall.domain.mall.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("common")
@Accessors(chain=true)
public class Common implements Serializable {

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 用户id  评论人id
     */
    private Integer userId;
    /**
     * 评论内容
     */
    private String context;
    /**
     * 创建评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String createTimeShow;
    /**
     * 评论得分
     * 1-5 星级  1，2 差评 3 中评  4，5 好评
     */
    private Integer score;


    /**
     *  一对多  一条评论对应 多条回复
     */
    @TableField(exist = false)
    private List<Reply> replyList;




    /**
     * 评论人的姓名
     */
    @TableField(exist = false)
    private String username;
}
