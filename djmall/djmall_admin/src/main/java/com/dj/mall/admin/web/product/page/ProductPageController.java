package com.dj.mall.admin.web.product.page;

import com.dj.mall.api.order.product.ProductService;
import com.dj.mall.api.order.user.UserService;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/product/")
public class ProductPageController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    /**
     * 去商品展示页面
     * @return
     */
    @RequestMapping("toList")
    public String toList(ModelMap map){
        try {
            String parentCode = "PRODUCT_TYPE";

            //查询数据库
            List<Dictionary> dicList = productService.findDictionaryByparentCode(parentCode);
            map.put("dicList", dicList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "product/list";
    }

    /**
     * 添加商品  下拉框运费 和物流名称  产品分类
     * @return 返回数据
     */
    @RequestMapping("toAdd")
    public String toAdd(Product product, ModelMap map){
        try {
            //下拉查询运费   0 位包邮
            List<Freight> freightAll = productService.findFreightAll();
            for (Freight freight : freightAll) {
                if(freight.getFreight().equals(SystemConstant.STRING_ZERO)){
                    freight.setFreight(SystemConstant.BAO_YOU);
                }
            }
            //下拉查询商品类型
            List<Dictionary> dictionaryList = productService.findDictionaryByProductType();
            map.put("freightAll", freightAll);
            map.put("dictionaryList", dictionaryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "product/add";
    }

    /**
     * 去修改商品
     * @return 返回数据
     */
    @RequestMapping("toUpdate/{id}")
    public String toUpdate(@PathVariable("id") Integer id, ModelMap map){
        try {
            //运费
            List<Freight> freightAll = productService.findFreightAll();
            for (Freight freight : freightAll) {
                if(freight.getFreight().equals(SystemConstant.STRING_ZERO)){
                    freight.setFreight(SystemConstant.BAO_YOU);
                }
            }
            //字典数据
            List<Dictionary> dictionaryList = productService.findDictionaryByProductType();
            //回显商品信息
            Product product = productService.findProductById(id);
            //回显sku
            List<ProductSku> productSkuList = productService.findProductSkuById(id);
            map.put("freightAll", freightAll);
            map.put("dictionaryList", dictionaryList);
            map.put("product", product);
            map.put("productSkuList", productSkuList);
            return "product/update";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }















    /**
     * 去 修改编辑Sku
     * @return
     */
    @RequestMapping("toUpdateEditor/{id}/{proId}")
    public String toUpdateEditor(@PathVariable("id") Integer id, @PathVariable("proId") Integer proId,  ModelMap map){
        try {
            ProductSku productSku = productService.toUpdateById(id);
            map.put("productSku", productSku);
            map.put("proId", proId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "product/update_editor";
    }



    /**
     * 去 修改库存sku   根据sku   id   去修改
     * @return
     */
    @RequestMapping("toUpdateCount/{id}/{proId}")
    public String toUpdateCount(@PathVariable("id") Integer id, @PathVariable("proId") Integer proId,  ModelMap map){
        try {
            ProductSku productSku = productService.toUpdateById(id);
            map.put("productSku", productSku);
            map.put("proId", proId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "product/update_count";
    }





    /**
     * 去评价  回显好评率
     * @return
     */
    @RequestMapping("toCommon")
    public String toCommon(Integer id,  ModelMap map){
        try {
            //计算好评率
            List<Common> commonList = userService.findCommonAllByProductId(id);
            //查询评论 根据product id 查询
            int num = SystemConstant.NUMBER_ZERO; //0
            for (Common common1 : commonList) {
                //字符串表示 时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                common1.setCreateTimeShow(df.format(common1.getCreateTime()));
                //计算好评率
                if(common1.getScore() == SystemConstant.NUMBER_FOUR|| common1.getScore() == SystemConstant.NUMBER_FIVE){
                    num++;
                }
            }
            Double goodRate =(double) num/commonList.size();
            //四舍五入
            Double a = (double)Math.round(goodRate*SystemConstant.NUMBER_ONE_HOUNDER) ;
            //评论好评率
            map.put("goodRate", a);
            map.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pro_order/common";
    }


    /**
     * 商家去回复    评论id  回复内容               回复类型  2
     * @return
     */
    @RequestMapping("toReply/{id}/{productId}")
    public String toReply(@PathVariable("id") Integer id, @PathVariable("productId") Integer productId, ModelMap map){
        map.put("commentId", id);
        map.put("productId", productId);
        return "pro_order/reply";
    }





}
