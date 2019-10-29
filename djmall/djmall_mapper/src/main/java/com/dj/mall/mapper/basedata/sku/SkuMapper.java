package com.dj.mall.mapper.basedata.sku;


import com.dj.mall.domain.basedata.sku.entiy.ProductAttr;
import com.dj.mall.domain.basedata.sku.entiy.ProductAttrValue;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SkuMapper  {
    /**
     * 属性  属性值 展示
     * @return
     * @throws DataAccessException
     */
    List<ProductAttr> findProductAttrToAttrValueAll() throws DataAccessException;

    /**
     * 添加属性名去重
     */
    ProductAttr findProductAttrToUniq(String attrName)throws DataAccessException;
    /**
     * 添加属性名
     */
    void insertProductAttr(ProductAttr productAttr) throws DataAccessException;
    /**
     * 根据属性名id查询属性值
     */
    List<ProductAttrValue> findProductAttrValueByAttrId(Integer attrId) throws DataAccessException;


    /**
     * 属性值添加去重
     */
    ProductAttrValue findProductAttrValueByAttrIdAndAttrName(ProductAttrValue productAttrValue)throws DataAccessException;

    /**
     * 添加属性值
     */
    void insertProductAttrValue(ProductAttrValue productAttrValue)throws DataAccessException;

    /**
     * 移除属性值
     */
    void  deleteProductAttrValueById(Integer id)throws DataAccessException;







    /**
     * SKU相关
     */



    /**
     * 添加商品类型  级联字典商品类型
     */
    void insertProductSkuGmByDictionary(ProductSkuGm productSkuGm)throws DataAccessException;

    /**
     * SKU展示列表
     */
    List<ProductSkuGm> findProductSkuGmAll()throws DataAccessException;

    /**
     * 关联表  被选中的
     * @param productType
     * @return
     * @throws DataAccessException
     */
    List<ProductAttr> findProductAttrToRelation(String productType) throws DataAccessException;

    /**
     * 关联表展示
     */
    List<ProductAttr> findProductAttrToRelationAll() throws DataAccessException;


    /**
     * 拼接属性值
     */
    ProductAttrValue getProAttrValue(Integer id)throws DataAccessException;







}
