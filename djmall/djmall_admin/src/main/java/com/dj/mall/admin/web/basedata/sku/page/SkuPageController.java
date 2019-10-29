package com.dj.mall.admin.web.basedata.sku.page;


import com.dj.mall.api.order.basedata.sku.SkuService;
import com.dj.mall.domain.basedata.sku.entiy.ProductAttr;
import com.dj.mall.domain.basedata.sku.entiy.ProductAttrValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/sku/")
public class SkuPageController {

    @Autowired
    private SkuService skuService;

    /**
     * 去展示属性  属性值
     * @return
     */
    @RequestMapping("toList")
    public String toList(){
        return "basedata/sku/list";
    }

    /**
     * 去展示属性  属性值
     * @param id
     * @param attrName  属性名回显
     * @param map  返回数据
     * @return
     */
    @RequestMapping("relationAttrValue/{id}/{attrName}")
    public String relationAttrValue(@PathVariable("id") Integer id, @PathVariable("attrName") String attrName, ModelMap map){
        try {
            List<ProductAttrValue> attrValueList = skuService.findProductAttrValueByAttrId(id);
            map.put("id", id);
            map.put("attrName", attrName);
            map.put("attrValueList", attrValueList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "basedata/sku/relation_attr_value";
    }








    /**
     * 去展示sku
     * @return
     */
    @RequestMapping("toSkuList")
    public String toSkuList(){
        return "basedata/sku/sku_list";
    }



    /**
     * 关联属性
     * @return
     */
    @RequestMapping("relationAttrSKU/{productType}")
    public String relationAttrSKU(@PathVariable("productType") String productType, ModelMap map) throws  Exception{
        try {
            /**
             * 已选中的展示 勾选
             */
            List<ProductAttr> productAttrList = skuService.findProductAttrToRelation(productType);
            map.put("productAttrList", productAttrList);
            /**
             * 所有关联资源展示列表
             */
            List<ProductAttr> productAttrAllList = skuService.findProductAttrToRelationAll();
            for (ProductAttr productAttr : productAttrAllList) {
                // 根据属性名id    获取属性值
                ProductAttrValue attrValue = skuService.getProAttrValue(productAttr.getId());
                productAttr.setAttrValueShow(attrValue.getAtVaShow());
            }
            map.put("productAttrAllList", productAttrAllList);
            map.put("productType", productType);
            return "basedata/sku/sku_relation_list";
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }




}
