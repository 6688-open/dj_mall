package com.dj.mall.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Reply;
import com.dj.mall.domain.product.entiy.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 商品展示
     */
    Page<Product> findProductAll(Page page, @Param("product") Product product) throws DataAccessException;

    /**
     * 根据商品id  去查询订单表  计算订单数量
     * @param productId
     * @return
     * @throws DataAccessException
     */
    Integer getOrderByProductIdCount(Integer productId)throws DataAccessException;

    /**
     * 去展示时 模糊查显示复选框    parentCode = PRODUCT_TYPE
     */
    List<Dictionary> findDictionaryByparentCode(String parentCode) throws DataAccessException;

    /**
     * 添加商品  下拉框运费 和物流名称
     */
    List<Freight> findFreightAll()throws DataAccessException;

    /**
     * 添加商品  下拉框产品分类 Dictionary
     * @return
     * @throws DataAccessException
     */
    List<Dictionary> findDictionaryByProductType()throws DataAccessException;






    /**
     *  商品对应 添加属性 属性值  展示列表
     * @param productCode  通过商品code查询
     * @return
     */
    List<ProductSkuGm> findProductAttrValueByProductCode(String productCode ) throws DataAccessException;




    /**
     * 查询评论展示     指定商品 商品的所有评论
     * @param common    根据商品id  查询
     * @return      返回集合
     * @throws DataAccessException
     */
    Page<Common> findCommonByProductId(Page page, @Param("common") Common common)throws DataAccessException;

    /**
     * 商家回复
     * @param reply
     * @throws DataAccessException
     */
    void insertReply(Reply reply)throws DataAccessException;

}
