package com.dj.mall.api.order.basedata.sku;


import com.dj.mall.domain.basedata.sku.entiy.ProductAttr;
import com.dj.mall.domain.basedata.sku.entiy.ProductAttrValue;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SkuService {

    /**
     * 属性  属性值 展示
     * @return 返回 list
     * @throws Exception
     */
    List<ProductAttr> findProductAttrToAttrValueAll() throws Exception;

    /**
     * 添加属性名去重
     * @param attrName  根据 attrName 去重
     * @return  返回数据 ProductAttr
     * @throws DataAccessException
     */
    ProductAttr findProductAttrToUniq(String attrName)throws Exception;
    /**
     *  添加属性名
     * @param productAttr 传递参数
     * @throws Exception
     */
    void insertProductAttr(ProductAttr productAttr) throws Exception;
    /**
     * 根据属性名id查询属性值
     * @param attrId  id查询属性值
     * @return 返回数据
     * @throws Exception
     */
    List<ProductAttrValue> findProductAttrValueByAttrId(Integer attrId) throws Exception;


    /**
     * 属性值添加去重
     * @param productAttrValue   ValueByAttrI  AttrName
     * @return  返还数据
     * @throws Exception
     */
    ProductAttrValue findProductAttrValueByAttrIdAndAttrName( ProductAttrValue productAttrValue)throws Exception;

    /**
     * 添加属性值
     * @param productAttrValue 传递参数
     * @throws Exception
     */
    void insertProductAttrValue(ProductAttrValue productAttrValue)throws Exception;
    /**
     * 移除属性值
     * @param id 根据id 移出
     * @throws Exception
     */
    void  deleteProductAttrValueById(Integer id)throws Exception;
    /**
     * 关联表 已经被关联资源
     * @param productType 根据 CODE 查询
     * @return 返还 数据
     * @throws Exception
     */
    List<ProductAttr> findProductAttrToRelation(String productType) throws Exception;

    /**
     * 关联表展示
     * @return  返回数据
     * @throws Exception
     */
    List<ProductAttr> findProductAttrToRelationAll() throws Exception;


    /**
     * 添加商品类型  级联字典商品类型
     * @param productSkuGm  传递参数
     * @throws Exception
     */
    void insertProductSkuGmByDictionary(ProductSkuGm productSkuGm)throws Exception;

    /**
     *  SKU展示列表
     * @return  返回数据
     * @throws Exception
     */
    List<ProductSkuGm> findProductSkuGmAll()throws Exception;



    /**
     * 拼接属性值
     * @param id 根据 id 查询
     * @return  ProductAttrValue 返回数据
     * @throws Exception
     */
    ProductAttrValue getProAttrValue(Integer id)throws Exception;


    /**
     * 删除已关联属性   保存最新的属性
     * @param productType   根据 先删除
     * @param productAttrIds  添加关联id
     * @throws Exception
     */
    void deleteAndSave(String  productType, Integer[] productAttrIds)throws Exception;








}
