package com.dj.mall.constant;

public interface SystemConstant {
    //页数
    Integer PAGESIZE_NUMBER = 8;
    //页数
    Integer PAGESIZE_NUMBER_FOUR = 4;
    //商户
    Integer LEVER_NUMBER_ONE = 1;
    //管理员
    Integer LEVER_NUMBER_TWO = 2;
    //管理员
    Integer LEVER_NUMBER_THREE = 3;
    //正常状态
    Integer STATUS_NUMBER_ONE = 1;
    //未激活状态
    Integer STATUS_NUMBER_TWO = 2;
    // 重置密码  1
    Integer  ACTIVATE_ONE = 1;
    // 未重置密码  2
    Integer  ACTIVATE_TWO = 2;

    //type 邮箱类型
    Integer TYPE_NUMBER = 1;
    //163邮箱判断返回
    Integer  EMAIL_CODE_TOW_ZERO_THREE = 203;
    //qq邮箱判断返回
    Integer  EMAIL_CODE_TOW_ZERO_FOUR = 204;
    //注册成功 状态码
    Integer  EMAIL_CODE_TOW_ZERO_FIVE = 205;
    //200成功状态码
    Integer  SUCCESS_CODE = 200;
    //Log物流 parent_code  =  LOG
    String   LOG = "LOG";


    String YU_MING = "poxgj8cq6.bkt.clouddn.com/";
    String HTTP = "http://";



    String STRING_ZERO = "0";
    String STRING_ONE = "1";
    String BAO_YOU = "包邮";


    Integer NUMBER_ZERO = 0;
    Integer NUMBER_ONE_HOUNDER = 100;


    //验证码类型 1   忘记密码手机验证码
    String CODETYPE_ONE = "1";

    //设置过期时间
    Integer ONE_MINUETS_TIME = 60001;
    //如果缓存中的数据次数 大于 3 的话
    Integer NUMBER_THREE = 3;
    //存入次数
    Integer NUMBER_ONE = 1;
    //24小时毫秒值
    Integer  ONE_HOUR = 864000;


   // 订单状态   1 待支付  2 已支付 3 已完成 4 已取消
    Integer NUMBER_SON_NUM_STATUS_ONE = 1;
    Integer NUMBER_SON_NUM_STATUS_TWO = 2;
    Integer NUMBER_SON_NUM_STATUS_THREE = 3;
    Integer NUMBER_SON_NUM_STATUS_FOUR= 4;



    //发送邮箱 拼接激活路径
    String HTTP_SEND_EMAIL = "http://127.0.0.1:8090/admin/user/updateStatus?id=";


    Integer NUMBER_FOUR = 4;
    Integer NUMBER_FIVE= 5;

    Integer NUMBER_TOW_ZERO_ZERO = 200;

    Integer NUMBER_TEN =10 ;

    Integer NUMBER_FOUR_FIVE = 45;
    Integer NUMBER_THREE_SIX_ZERO = 360;

    Integer NUMBER_FIFTTER = 15;


    Integer NUMBER_SIX_ZERO = 60;

    Integer ZERO = 0;
    Integer ONE = 1;
    Integer TWO = 2;
    Integer THREE = 3;
    Integer FOUR = 4;
    Integer FIVE = 5;
    Integer SIX = 6;
    Integer SEVEN = 7;
    Integer EIRHT = 8;
    Integer NINE = 9;
    Integer TEN = 10;
    Integer ELEVEN = 11;
    Integer TWELVE = 12;

}
