package com.dj.mall.api.order.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Reply;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductService /*extends IService<Product> */{





    /**
     * 商品展示           service
     * @return 返回数据
     * @throws Exception
     */
    Page<Product> findProductAll(Page page, Product product) throws Exception;
    /**
     * 根据商品id  去查询订单表  计算订单数量
     * @param productId
     * @return
     * @throws Exception
     */
    Integer getOrderByProductIdCount(Integer productId)throws Exception;



    /**
     * 去展示时 模糊查显示复选框    parentCode = PRODUCT_TYPE
     * @param parentCode  根据商品 parentCode = PRODUCT_TYPE 查询
     * @return  返回数据
     * @throws Exception
     */
    List<Dictionary> findDictionaryByparentCode(String parentCode) throws Exception;

    /**
     * 根据id查询 数据  去修改
     * @param id  根据id查询
     * @return  返回数据
     * @throws Exception
     */
    Product findProductById(Integer id)throws Exception;

    /**
     * 根据商品Id取查询SKU 回显
     * @param productId  根据productId 查询
     * @return
     * @throws Exception
     */
    List<ProductSku> findProductSkuById(Integer productId)throws Exception;

    /**
     * 根据id修改状态  上架 下架
     * @param product 传递参数
     * @throws Exception
     */
    void updateProductStatusById(Product product)throws Exception;

    /**
     * 添加商品  下拉框运费 和物流名称
     */
    List<Freight> findFreightAll()throws Exception;
    /**
     * 添加商品  下拉框产品分类 Dictionary
     * @return
     * @throws DataAccessException
     */
    List<Dictionary> findDictionaryByProductType()throws Exception;

    /**
     * 添加  修改 上篇去重
     * @param productName
     * @return  返回数据
     * @throws Exception
     */
    Product findProductByProductNameToUnique(String productName)throws Exception;

    /**
     * 添加商品
     * @param product  传递参数
     * @throws Exception
     */
    void insertProductToAdd(byte[] bytes, Product product) throws Exception;

    /**
     * 商品修改
     * @throws Exception
     */
    void updateProduct(byte[] bytes, Product product)throws Exception;



    /**
     *  商品对应 添加属性 属性值  展示列表
     * @param productCode  通过商品code查询
     * @return
     */
    List<ProductSkuGm> findProductAttrValueByProductCode(String productCode ) throws Exception;


    /**
     * 修改SKU下架
     * @param skuStatus 0 代表下架
     * @throws Exception
     */
    void updateProductSkuStatus(Integer id, Integer skuStatus) throws Exception;

    /**
     * 根据ProductSku id 去查询一条数据
     * @param id
     * @return
     * @throws Exception
     */
    ProductSku toUpdateById(Integer id)throws Exception;

    /**
     *  修改默认   将之前的默认修改为否
     * @param 0 代表下架
     * @throws Exception
     */
    void updateProductSkuIsDefault(List<ProductSku> list) throws Exception;

    /**
     * 根据商品ID 和 默认1 查找已经被默认的数据
     * @param productId
     * @param skuDefault
     * @return
     * @throws Exception
     */
    ProductSku  updateIsDefaultAndToNotDefault(Integer productId, String  skuDefault) throws Exception;

    /**
     * 修改库存
     * @throws Exception
     */
    void updateProductSkuCount(ProductSku productSku)throws Exception;




    /**
     * 重建索引
     * fullOrDelta  管理员  重建索引  参数 full   新增时 detel 增量索引
     * @return
     * @throws Exception
     */
    Integer makeSolr(String fullOrDelta) throws Exception;



    /**
     * 查询评论展示     指定商品 商品的所有评论
     * @param   common   根据商品id  查询
     * @return      返回集合
     * @throws Exception
     */
    Page<Common> findCommonByProductId(Page page, Common common)throws Exception;


    /**
     * 商家回复
     * @param reply
     * @throws DataAccessException
     */
    void insertReply(Reply reply)throws Exception;


    /**
     * 导入商品  批量
     * @param productList
     * @throws Exception
     */
    Boolean addProAll(List<Product> productList)throws Exception;


}
