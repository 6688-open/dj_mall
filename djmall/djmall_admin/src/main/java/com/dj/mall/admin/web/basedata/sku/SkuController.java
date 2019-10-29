package com.dj.mall.admin.web.basedata.sku;


import com.dj.mall.api.order.basedata.sku.SkuService;
import com.dj.mall.common.ResultModel;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.basedata.sku.entiy.ProductAttr;
import com.dj.mall.domain.basedata.sku.entiy.ProductAttrValue;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sku/")
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 属性  属性值 展示
     * @return  返回数据 map
     */
    @RequestMapping("list")
    public ResultModel list(){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<ProductAttr> productAttrList = skuService.findProductAttrToAttrValueAll();
            map.put("productAttrList", productAttrList);
            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 添加属性
     * @param productAttr  传递参数
     * @return  返回
     */
    @RequestMapping("insert")
    public ResultModel insert(ProductAttr productAttr){
        try {
            /**
             * 判断去重
             */
            ProductAttr productAttr1 = skuService.findProductAttrToUniq(productAttr.getAttrName());
            if(productAttr1 != null ){
                return new ResultModel().error("属性名已存在");
            }
            /**
             * 添加
             */
            skuService.insertProductAttr(productAttr);
            return new ResultModel().success("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }


    /**
     * 添加属性值
     * @param productAttrValue 传递参数
     * @return 接收参数
     */
    @RequestMapping("insertAttrValue")
    public ResultModel insertAttrValue(ProductAttrValue productAttrValue){
        Map<String, Object> map = new HashMap<>();
        try {
            /**
             * 去重
             */
            ProductAttrValue attrValue = skuService.findProductAttrValueByAttrIdAndAttrName(productAttrValue);
            if(null != attrValue){
                return new ResultModel().error("属性值已存在");
            }
            /**
             * 添加
             */
            skuService.insertProductAttrValue(productAttrValue);
            map.put("attrId", productAttrValue.getAttrId());
            map.put("attrValue", productAttrValue.getAttrValue());
            return new ResultModel().success(SystemConstant.SUCCESS_CODE,"success",map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 移出属性值
     * @param id 根据 id 移除
     * @return
     */
    @RequestMapping("remve/{id}")
    public ResultModel remve(@PathVariable("id") Integer id){
        try {
            skuService.deleteProductAttrValueById(id);
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }


    /**
     * SKU展示列表
     * @return 返回map
     */
    @RequestMapping("skuList")
    public ResultModel skuList(){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<ProductSkuGm> skuList = skuService.findProductSkuGmAll();
            map.put("skuList", skuList);
            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }


    /**
     * 添加资源  删除  对应资源
     * @param productType   接收 商品 CODE  删除已关联的所有属性名
     * @param productAttrIds  保存的属性
     * @return
     */
    @RequestMapping("savaAndDelRela/{productType}")
    public ResultModel<Object> savaAndDelRela(@PathVariable("productType")String  productType, Integer[] productAttrIds){
        try {
            if(null == productAttrIds){
                return new ResultModel<>().error("请至少选择一项");
            }
            skuService.deleteAndSave(productType, productAttrIds);
            return new ResultModel<>().success("添加成功");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }

    }










}
