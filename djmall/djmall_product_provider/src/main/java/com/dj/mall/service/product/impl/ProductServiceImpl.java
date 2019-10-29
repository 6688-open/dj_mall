package com.dj.mall.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.mall.api.order.cmpt.RedisService;
import com.dj.mall.api.order.product.ProductService;
import com.dj.mall.api.order.product.ProductSkuService;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Reply;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import com.dj.mall.mapper.product.ProductMapper;
import com.dj.mall.util.QiNiuUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private HttpSolrClient httpSolrClient;


    /**
     * 商品展示
     * @return 返回数据
     * @throws Exception
     */
    @Override
    public Page<Product> findProductAll(Page page, Product product) throws Exception {
        return productMapper.findProductAll(page, product);
    }

    /**
     * 根据商品id  去查询订单表  计算订单数量
     * @param productId  根据商品id
     * @return
     * @throws Exception
     */
    @Override
    public Integer getOrderByProductIdCount(Integer productId) throws Exception {
        return productMapper.getOrderByProductIdCount(productId);
    }

    /**
     * 去展示时 模糊查显示复选框    parentCode = PRODUCT_TYPE
     * @param parentCode  根据商品 parentCode = PRODUCT_TYPE 查询
     * @return  返回数据
     * @throws Exception
     */
    @Override
    public List<Dictionary> findDictionaryByparentCode(String parentCode) throws Exception {
        //根据 parentCode  从缓存中查询list
        List<Dictionary> hashValues = redisService.getHashValues(parentCode);
        //缓存中有数据从缓存中获取  否则从数据库查询
        if(hashValues.size() != SystemConstant.NUMBER_ZERO){  //0
            return hashValues;
        }
        //数据库查询list
        List<Dictionary> dictionaryList = productMapper.findDictionaryByparentCode(parentCode);
        //再将查出来的数据保存到缓存
        for (Dictionary dictionary : dictionaryList) {
            redisService.pushHash(dictionary.getParentCode(),dictionary.getCode(), dictionary);
        }
        return dictionaryList;

    }
    /**
     * 根据id查询 数据
     * @param id  根据id查询
     * @return  返回数据
     * @throws Exception
     */
    @Override
    public Product findProductById(Integer id) throws Exception {
        return this.getById(id);
    }
    /**
     * 根据商品Id取查询SKU 回显
     * @param productId  根据productId 查询
     * @return
     * @throws Exception
     */
    @Override
    public List<ProductSku> findProductSkuById(Integer productId) throws Exception {
        QueryWrapper<ProductSku> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        queryWrapper.orderByDesc("is_default");
        return productSkuService.list(queryWrapper);
    }

    /**
     * 根据id修改状态  上架 下架
     * @param product 传递参数
     * @throws Exception
     */
    @Override
    public void updateProductStatusById(Product product) throws Exception {
        this.updateById(product);
    }

    /**
     * 添加商品   修改   下拉框运费 和物流名称
     * @return 返回数据
     * @throws DataAccessException
     */
    @Override
    public List<Freight> findFreightAll() throws Exception {
        return productMapper.findFreightAll();
    }
    /**
     * 添加商品    修改     下拉框产品分类 Dictionary
     * @return
     * @throws Exception
     */
    @Override
    public List<Dictionary> findDictionaryByProductType() throws Exception {
        //通过缓存获取 parentCode =    PRODUCT_TYPE 的所有子  缓存没有去查询数据库
        List<Dictionary> hashValues = redisService.getHashValues("PRODUCT_TYPE");
        //缓存中有数据从缓存中获取  否则从数据库查询
        if(hashValues.size() != SystemConstant.NUMBER_ZERO){//判断没有数据
            return hashValues;
        }
        //从数据库里查询
        List<Dictionary> dictionaryList= productMapper.findDictionaryByProductType();
        //再将查出来的数据保存到缓存
        for (Dictionary dictionary : dictionaryList) {
            redisService.pushHash(dictionary.getParentCode(),dictionary.getCode(), dictionary);
        }
        return dictionaryList;
    }
    /**
     * 添加  修改 商品去重
     * @param productName
     * @return  返回数据
     * @throws Exception
     */
    @Override
    public Product findProductByProductNameToUnique(String productName) throws Exception {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(productName)){
            queryWrapper.eq("product_name", productName);
        }
        return this.getOne(queryWrapper);
    }
    /**
     * 添加商品
     * @param product  传递参数
     * @throws Exception
     */
    @Override
    public void insertProductToAdd(byte[] bytes,Product product)throws Exception {
        String uuName = UUID.randomUUID().toString();
        QiNiuUtils.put64image(bytes, uuName);
        String finalName = SystemConstant.YU_MING+uuName;
        product.setPicture(finalName);
        //DELETE方法 需要截取33位  判断第一次非空
      /*  if(!StringUtils.isEmpty(user1.getFileName())){
            String deleteName = SystemConstant.HTTP+user1.getFileName();
            QiNiuUtils.delete(deleteName);
        }*/
        this.save(product);
        List<ProductSku> list = new ArrayList<>();

        for (int i = 0; i < (product.getSkuPrice()).length ; i++) {
            System.out.println( (product.getSkuPrice()).length);
            ProductSku productSku = new ProductSku();
            productSku.setProductId(product.getId());
            //默认第一条是 默认  是  其他为否   0  代表否   1 代表是
            if(i == SystemConstant.NUMBER_ZERO){
                productSku.setIsDefault(SystemConstant.STRING_ONE);
            } else {
                productSku.setIsDefault(product.getIsDefault()[i]);
            }
            productSku.setSkuCount(product.getSkuCount()[i]);
            productSku.setSkuStatus(product.getSkuStatus()[i]);
            productSku.setSkuRate(product.getSkuRate()[i]);
            productSku.setSkuPrice(product.getSkuPrice()[i]);
            System.out.println(product.getSkuAttrValueNames()[i]);
            productSku.setSkuAttrValueNames(product.getSkuAttrValueNames()[i]);
            productSku.setSkuAttrValueIds(product.getSkuAttrValueIds()[i]);
            productSku.setSkuAttrNames(product.getSkuAttrNames()[i]);
            productSku.setSkuAttrIds(product.getSkuAttrIds()[i]);

            list.add(productSku);
        }
        //批量新增
        productSkuService.saveBatch(list) ;
    }

    /**
     * 商品修改
     * @param product  传参
     * @throws Exception
     */
    @Override
    public void updateProduct(byte[] bytes, Product product) throws Exception {
        if(null != bytes){
            //上传图片
            Product product1 = this.findProductById(product.getId());
            String uuName = UUID.randomUUID().toString();
            //上传到七牛云
            QiNiuUtils.put64image(bytes, uuName);
            String finalPic = SystemConstant.YU_MING+uuName;
            //保存到数据库
            product.setPicture(finalPic);
            //DELETE方法 需要截取33位  判断第一次非空
            if(!StringUtils.isEmpty(product1.getPicture())){
                String deleteName = SystemConstant.HTTP+product1.getPicture();
                QiNiuUtils.delete(deleteName);
            }
        }
       //修改保存
        this.updateById(product);
    }

    /**
     *  商品对应 添加属性 属性值  展示列表
     * @param productCode  通过商品code查询
     * @return
     */
    @Override
    public List<ProductSkuGm> findProductAttrValueByProductCode(String productCode) throws Exception {
        return productMapper.findProductAttrValueByProductCode(productCode);
    }


    /**
     *
     * @param skuStatus 0 代表下架
     * @throws Exception
     */
    @Override
    public void updateProductSkuStatus(Integer id, Integer skuStatus) throws Exception {
        ProductSku productSku = new ProductSku();
        productSku.setId(id);
        productSku.setSkuStatus(skuStatus);
        productSkuService.updateById(productSku);
    }



    /**
     * 根据ProductSku id 去查询一条数据
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ProductSku toUpdateById(Integer id) throws Exception {
        return productSkuService.getById(id);
    }

    /**
     *  修改默认   将之前的默认修改为否
     * @param list   代表下架
     * @throws Exception
     */
    @Override
    public void updateProductSkuIsDefault(List<ProductSku> list) throws Exception {
        productSkuService.updateBatchById(list);
    }
    /**
     * 根据商品ID 和 默认1 查找已经被默认的数据
     * @param productId
     * @param skuDefault
     * @return
     * @throws Exception
     */
    @Override
    public ProductSku updateIsDefaultAndToNotDefault(Integer productId, String skuDefault) throws Exception {
        QueryWrapper<ProductSku>   queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        queryWrapper.eq("is_default", skuDefault);
        return productSkuService.getOne(queryWrapper);
    }

    /**
     *  修改库存
     * @param productSku  接收参数
     * @throws Exception
     */
    @Override
    public void updateProductSkuCount(ProductSku productSku) throws Exception {
        productSkuService.updateById(productSku);
    }



    /**
     * 重建索引  管理员可见
     * @return
     * @throws Exception
     */
    @Override
    public Integer makeSolr(String fullOrDelta) throws Exception {
        HttpClient httpClient = httpSolrClient.getHttpClient();
        //重做索引
        HttpGet httpGet = new HttpGet("http://localhost:8081/solr/slorCoreDJmallProduct/dataimport?command="+fullOrDelta+"-import");
        HttpResponse httpResponse = httpClient.execute(httpGet);
        return httpResponse.getStatusLine().getStatusCode();
    }
    /**
     * 查询评论展示     指定商品 商品的所有评论
     * @param   common   根据商品id  查询
     * @return      返回集合
     * @throws Exception
     */
    @Override
    public Page<Common> findCommonByProductId(Page page, Common common) throws Exception {
        return productMapper.findCommonByProductId(page, common);
    }
    /**
     * 商家回复
     * @param reply
     * @throws DataAccessException
     */
    @Override
    public void insertReply(Reply reply) throws Exception {
        productMapper.insertReply(reply);
    }

    /**
     *  导入商品  批量
     * @param productList
     * @throws Exception
     */
    @Override
    public Boolean addProAll(List<Product> productList) throws Exception {
        this.saveBatch(productList);
        return true;
    }




}
