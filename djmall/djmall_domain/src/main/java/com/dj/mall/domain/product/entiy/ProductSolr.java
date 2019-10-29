package com.dj.mall.domain.product.entiy;


import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

@Data
public class ProductSolr implements Serializable {

    @Field(value = "product_id")
    private Integer id;
    /**
     * 产品名
     */
    @Field(value = "product_name")
    private String productName;
    /**
     * 字典数据 商品类型
     */
    @Field(value = "dictionary_code")
    private String dictionaryCode;

    /**
     * 状态 1 上架 2 下架
     */
    @Field(value = "product_status")
    private Integer productStatus;
    /**
     * 运费id
     */
    @Field(value = "freight_id")
    private Integer freightId;
    /**
     * 图片
     */
    @Field(value = "picture")
    private String picture;
    /**
     * 产品描述
     */
    @Field(value = "product_describe")
    private String productDescribe;
    /**
     * 点赞量
     */
    @Field(value = "thumb_number")
    private Integer thumbNumber;
    /**
     * 订单量
     */
    @Field(value = "order_number")
    private Integer orderNumber;
    /**
     * 创建时间
     */
   /* @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;*/



    /**
     * 运费
     */
    @Field(value = "freight")
    private Integer freight;
    /**
     * 物流名称
     */
    @Field(value = "dictionary_name")
    private String dictionaryName;
















    /*商城展示相关*/
    /**
     * sku 价格
     */
    @Field(value = "priceShow")
    private double priceShow;
    /**
     * 库存
     */
    @Field(value = "countShow")
    private String countShow;
    /**
     * 折扣
     */
    @Field(value = "rateShow")
    private String rateShow;
    /**
     * 商品分类
     */
    @Field(value = "dictionaryNameShow")
    private String dictionaryNameShow;
    /**
     * 属性值 名称
     */
    @Field(value = "skuAttrValuesNames")
    private String skuAttrValuesNames;


    /**
     * sku  id
     */
    @Field(value = "id")
    private String proSkuId;
    /**
     * sku  id
     */
    @Field(value = "sku_is_default")
    private Integer isDefault;












}
