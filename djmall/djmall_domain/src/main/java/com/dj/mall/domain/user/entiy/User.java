package com.dj.mall.domain.user.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("djmall_user")
@Accessors(chain = true)
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别 1 男  2 女
     */
    private Integer sex;
    /**
     * 级别 1 商户 2 管理员
     */
    private Integer lever;
    /**
     * 状态 1 正常 2 未激活
     */
    private Integer status;
    /**
     * 6位数为重置密码 null 为
     */
    private String restPassword;
    /**
     * 是否被重置
     * 1 是
     * 2 无  默认无
     */
    private Integer isReset;
    /**
     * 昵称  买家
     */
    private String nickName;
    /**
     * 头像  买家
     */
    private String headImg;
    /**
     * 验证码类型
     * 1  忘记密码
     */
    @TableField(exist = false)
    private String codeType;






}
