package com.dj.mall.service.basedata.sku.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dj.mall.api.order.basedata.sku.ProductSkuGmService;
import com.dj.mall.api.order.basedata.sku.SkuService;
import com.dj.mall.domain.basedata.sku.entiy.ProductAttr;
import com.dj.mall.domain.basedata.sku.entiy.ProductAttrValue;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import com.dj.mall.mapper.basedata.sku.SkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private ProductSkuGmService productSkuGmService;


    /**
     * 属性  属性值 展示
     * @return 返回 list
     * @throws Exception
     */
    @Override
    public List<ProductAttr> findProductAttrToAttrValueAll() throws Exception {
        return skuMapper.findProductAttrToAttrValueAll();
    }

    /**
     * 添加属性名去重
     * @param attrName  根据 attrName 去重
     * @return  返回数据 ProductAttr
     * @throws Exception
     */
    @Override
    public ProductAttr findProductAttrToUniq(String attrName) throws Exception {
        return skuMapper.findProductAttrToUniq(attrName);
    }

    /**
     *  添加属性名
     * @param productAttr 传递参数
     * @throws Exception
     */
    @Override
    public void insertProductAttr(ProductAttr productAttr) throws Exception {
        skuMapper.insertProductAttr(productAttr);
    }

    /**
     * 根据属性名id查询属性值
     * @param attrId  id查询属性值
     * @return 返回数据
     * @throws Exception
     */
    @Override
    public List<ProductAttrValue> findProductAttrValueByAttrId(Integer attrId) throws Exception {
        return skuMapper.findProductAttrValueByAttrId(attrId);
    }

    /**
     * 属性值添加去重
     * @param productAttrValue   ValueByAttrI  AttrName
     * @return  返还数据
     * @throws Exception
     */
    @Override
    public ProductAttrValue findProductAttrValueByAttrIdAndAttrName(ProductAttrValue productAttrValue) throws Exception {
        return skuMapper.findProductAttrValueByAttrIdAndAttrName(productAttrValue);
    }

    /**
     * 添加属性值
     * @param productAttrValue 传递参数
     * @throws Exception
     */
    @Override
    public void insertProductAttrValue(ProductAttrValue productAttrValue) throws Exception {
        skuMapper.insertProductAttrValue(productAttrValue);
    }

    /**
     * 移除属性值
     * @param id 根据id 移出
     * @throws Exception
     */
    @Override
    public void deleteProductAttrValueById(Integer id) throws Exception {
        skuMapper.deleteProductAttrValueById(id);
    }



    /**
     * 添加商品类型  级联字典商品类型
     * @param productSkuGm
     * @throws Exception
     */
    @Override
    public void insertProductSkuGmByDictionary(ProductSkuGm productSkuGm) throws Exception {
        skuMapper.insertProductSkuGmByDictionary(productSkuGm);
    }


    /**
     * SKU展示列表
     * @return 返还数据
     * @throws Exception
     */
    @Override
    public List<ProductSkuGm> findProductSkuGmAll() throws Exception {
        return skuMapper.findProductSkuGmAll();
    }



    /**
     * 关联表 已经被关联资源
     * @param productType 根据 CODE 查询
     * @return 返还 数据
     * @throws Exception
     */
    @Override
    public List<ProductAttr> findProductAttrToRelation(String productType) throws Exception {
        return skuMapper.findProductAttrToRelation(productType);
    }

    /**
     * 关联表展示
     * @return  返回数据
     * @throws Exception
     */
    @Override
    public List<ProductAttr> findProductAttrToRelationAll() throws Exception {
        return skuMapper.findProductAttrToRelationAll();
    }

    /**
     * 拼接属性值
     * @param id 根据 id 查询
     * @return  ProductAttrValue 返回数据
     * @throws Exception
     */
    @Override
    public ProductAttrValue getProAttrValue(Integer id) throws Exception {
        return skuMapper.getProAttrValue(id);
    }




    /**
     * 删除已关联属性   保存最新的属性
     * @param productType   根据 先删除
     * @param productAttrIds  添加关联id
     * @throws Exception
     */
    @Override
    public void deleteAndSave(String productType, Integer[] productAttrIds) throws Exception {
        //先删除已经关联的属性
        QueryWrapper<ProductSkuGm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_type", productType);
        productSkuGmService.remove(queryWrapper);
        //组装数据  新增关联属性
        List<ProductSkuGm> list = new ArrayList();
        for (Integer productAttrId : productAttrIds) {
            ProductSkuGm productSkuGm = new ProductSkuGm();
            productSkuGm.setProductType(productType);
            productSkuGm.setAttrId(productAttrId);
            list.add(productSkuGm);
        }
        //批量保存
        productSkuGmService.saveBatch(list);
    }


}
