package com.dj.mall.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.domain.mall.entiy.Address;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Order;
import com.dj.mall.domain.mall.entiy.ShoppingCar;
import com.dj.mall.domain.product.entiy.Great;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import com.dj.mall.domain.user.entiy.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 商城展示列表
     */
    Page<Product> findProductAndSKUAll(Page page, @Param("product") Product product) throws DataAccessException;

    /**
     *  商品详情
     * @param id   根据id 查询
     * @return  返回 实体类 Product
     * @throws DataAccessException
     */
    Product findProductMsgById(Integer id) throws DataAccessException;


    /**
     * 根据ProductId 查询 sku 集合 属性值
     * @return 返回数据
     * @throws DataAccessException
     */
    List<ProductSku> findProductSkuByProductId(Integer productId)throws DataAccessException;

    /**
     *  根据 id 查询数据   查询价格   不同sku 对应不同的价格   点击单选按钮 价格改变
     * @param id  根据id 查询
     * @return  返回数据
     * @throws DataAccessException
     */
    ProductSku findProductSkuById(Integer id)throws DataAccessException;

    /**
     * 去查询用户信息  从redis 里取
     * @return  返回数据
     * @throws DataAccessException
     */
    User toUpdateMsg(Integer id)throws DataAccessException;

    /**
     * //添加购物车  同一用户   同一个proSkuId   没有的话是新增   有的话更新数量
     * @param userId  根据userId
     * @param proSkuId  proSkuId
     * @return  返回一条数据
     * @throws DataAccessException
     */
    ShoppingCar getShoppinngCarByUserIdAndProSkuId(@Param("userId") Integer userId, @Param("proSkuId") Integer proSkuId)throws DataAccessException;

    /**
     * 判断加入购物车是否已经有数据    同一用户   同一个proSkuId  有的话  修改
     * @param shoppingCar
     * @throws DataAccessException
     */
    void updateShoppingCarNumByUserIdAndProSkuId(ShoppingCar shoppingCar)throws DataAccessException;
    /**
     * 判断加入购物车是否已经有数据    同一用户   同一个proSkuId  没有的话  新增
     * 添加购物车
     * @param shoppingCar  传递参数
     * @throws DataAccessException
     */
    void insertShoppingCar(ShoppingCar shoppingCar)throws DataAccessException;

    /**
     *   根据用户id  去查询当前登录用户 的购物车信息
     * @param shoppingCar  传递参数 id、 redis 获取
     * @return  返回数据
     * @throws DataAccessException
     */
    List<ShoppingCar> findShoppingCarById(ShoppingCar shoppingCar)throws DataAccessException;

    /**
     *购物车删除已选中的商品  批量删除
     * @param ids  参数
     * @throws DataAccessException
     */
    void deleteShoppingCarByIds(@Param("ids") Integer [] ids)throws DataAccessException;

    /**
     * 根据购物车id  去查询 一条数据
     * @return 返回 ShoppingCar
     * @throws Exception
     */
    ShoppingCar getShoppingCarById(Integer id)throws DataAccessException;

    /**
     * 根据id 修改 购物车的数量
     * @param id
     * @throws DataAccessException
     */
    void updateShoppingNumCarById(@Param("id") Integer id, @Param("num") Integer num)throws DataAccessException;

    /**
     * 选中 立即结算  价格 邮费 商品个数 回显
     * @param ids  根据传来的ids进行查询
     * @return 返回数据
     * @throws DataAccessException
     */
    List<ShoppingCar> getShoppingCarByIds(@Param("ids") Integer[] ids)throws DataAccessException;


    /**
     *   根据前台传来的ids   去展示要确认的订单  进入确认订单页面
     * @param  ids  参数
     * @return  返回数据
     * @throws DataAccessException
     */
    List<ShoppingCar> findShoppingCarByIds(@Param("ids") Integer [] ids)throws DataAccessException;

    /**
     * 在确认订单展示列表  回显登录人的地址列表集合
     * @param userId  根据userId  查询
     * @return
     * @throws DataAccessException
     */
    List<Address> findAddressByUserIdToOrderList(Integer userId)throws DataAccessException;

    /**
     *  在确认订单展示列表   确认加入订单表  状态为待支付     减库存  删除对应购物车
     * @param order
     * @throws DataAccessException
     */
    void insertOrder(@Param("order") List<Order> order) throws DataAccessException;


    /**
     * 查询所有订单展示  商户  管理员可见
     * @return
     * @throws DataAccessException
     */
    List<Order> findOrderAll(@Param("lever") Integer lever, @Param("sellerId") Integer sellerId)throws DataAccessException;

    /**
     * 卖家去发货 将待发货2   修改成已发货，待收货状态  3
     * @param orderSonNum 子集订单
     * @param message  已发货
     * @throws DataAccessException
     */
    void updateMessage(@Param("orderSonNum") String orderSonNum, @Param("message") Integer message)throws DataAccessException;


    /**
     * 前台获取  proSkuId  从redis获取userId  查询是否已经点赞过    如果已经点赞过就不能再点赞
     * @param userId
     * @param proSkuId
     * @return
     * @throws DataAccessException
     */
    Great getGreatByUserIdAndProSkuId(@Param("userId")Integer userId, @Param("proSkuId")Integer proSkuId)throws DataAccessException;

    /**
     * 查询不空 说明已经点赞了 取消点赞  根据查询的id 进行删除
     * @param id  根据id 取消点赞
     * @throws DataAccessException
     */
    void deleteGreatById(Integer id)throws DataAccessException;

    /**
     * 保存点赞表
     *    @param userId  用户id
     *     @param proSkuId  商品属性id
     * @throws DataAccessException
     */
    void addGreat(@Param("userId")Integer userId, @Param("proSkuId")Integer proSkuId)throws DataAccessException;

    /**
     *  //修改商品表 点赞数量 条件 id     thumbNumber
     * @param id
     * @param thumbNumber
     * @throws DataAccessException
     */
    void updateProductThumbById(@Param("id")Integer id, @Param("thumbNumber")Integer thumbNumber)throws DataAccessException;


    /**
     * 根据productSku 中productId查询 product表中的卖家userId
     * @param id   根据id 查询商品表
     * @return
     * @throws DataAccessException
     */
    Product getProductById(Integer id)throws DataAccessException;


    /**
     * 直接去商品展示页面    查询好评率
     * @param productId
     * @return
     * @throws DataAccessException
     */
    List<Common> findCommonAllByProductId(Integer productId)throws DataAccessException;






























}
