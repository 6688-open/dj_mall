package com.dj.mall.api.order.user;


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
import java.util.Map;

public interface UserService /*extends IService<User>*/ {

    /**
     * 用户登录
     * @param user 传递参数
     * @return User 返还数据
     * @throws Exception
     */
    User findUserByUsernameAndPassword(User user) throws Exception;

    /**
     * 注册去重
     * @param username 用户名
     * @param phone  手机号
     * @param email 邮箱
     * @return User 返还数据
     * @throws Exception
     */
    User findUserToUniq(String username, String phone, String email, String nickName) throws Exception;

    /**
     * 用户注册
     * @param user 传递参数
     * @throws Exception
     */
    Integer addUserToRegister(User user) throws Exception;

    /**
     * 商户邮箱激活 修改状态
     * @param id  根据 id 激活修改状态
     * @throws Exception
     */
    void updateStatusToUse(Integer id)throws Exception;

    /**
     * 忘记密码 获取验手机证码
     * @param user  传递参数
     * @throws Exception
     */
    void saveAndRemove(User user) throws Exception;

    /**
     * 忘记密码  发送邮箱
     * @param user 传递参数
     * @param code  判断验证码 是否一致
     * @throws Exception
     */
    void updateResetPasswordAndSendEmail(User user, String code, String pwd) throws Exception;

    /**
     * 用户展示
     * @param pageNo  当前页
     * @param user  传递参数
     * @return 返还数据
     * @throws Exception
     */
    Map<String, Object> findUserAll(Integer pageNo, User user)throws Exception;

    /**
     * 批量删
     * @param ids 根据 ids 删除
     * @throws Exception
     */
    void delMoreUser(Integer[] ids) throws Exception;
    /**
     * 批量激活
     * @param ids 根据 ids 激活
     * @throws Exception
     */
    void updateStatus(Integer[] ids) throws Exception;

    /**
     * 修改用户
     * @param id  根据id 修改
     * @throws Exception
     */
    User toUpdateUser(Integer id)throws Exception;
    /**
     * 修改用户
     * @param user 传递参数
     * @throws Exception
     */
    void updateUser(User user)throws Exception;

    /**
     * 重置密码 发送邮件
     * @param id  根据id 重置  发送邮箱
     * @throws Exception
     */
    void updatePasswordAndSendEmail(Integer id)throws Exception;
    /**
     * 当使用重置密码登陆时  去修改密码
     * @param user 传递参数
     * @throws Exception
     */
    void updateAndRestPasswordUpdateIsReset(User user)throws Exception;




    /**
     * 手机号码登录验证  前台输入的验证码和 之前存的redis里的进行比较  一天3次  时间超时 验证
     * @param user
     * @param phoneCode
     * @return
     */
    void findUserByPhoneAndRedisPhoneCode(User user, String phoneCode) throws Exception;



    /**
     * 查询所有订单展示  商户  管理员可见
     * @return
     * @throws DataAccessException
     */
    List<Order> findOrderAll(Integer lever, Integer sellerId)throws Exception;
    /**
     * 卖家去发货 将待发货2   修改成已发货，待收货状态  3
     * @param orderSonNum 子集订单
     * @param message  已发货
     * @throws Exception
     */
    void updateMessage(String orderSonNum,  Integer message)throws Exception;





















    /**
     * 通过solr 查询商城展示列表
     * @param pageNo  参数
     * @param product  传递参数
     * @return  返回map
     * @throws Exception
     */
    Map<String, Object> findProductBySolr(Integer pageNo, Product product)throws Exception;


    /**
     * 商城展示列表
     * @param page  分页page对象
     * @param product  模糊查对象
     * @return  返回实体类
     * @throws Exception
     */
    Page<Product> findProductAndSKUAll(Page page, @Param("product") Product product) throws Exception;
    /**
     *  商品详情
     * @param id   根据id 查询
     * @return  返回 实体类 Product
     * @throws Exception
     */
    Product findProductMsgById(Integer id) throws Exception;

    /**
     * 根据商品id 查询对应所有的sku 属性值
     * @param productId  根据商品id  查询
     * @return  返回ProductSku 数据
     * @throws Exception
     */
    List<ProductSku> findProductSkuByProductId(Integer productId)throws Exception;
    /**
     *  根据 id 查询数据   查询价格   不同sku 对应不同的价格   点击单选按钮 价格改变
     * @param id  根据id 查询
     * @return  返回数据
     * @throws DataAccessException
     */
    ProductSku findProductSkuById(Integer id)throws Exception;

    /**
     * 去查询用户信息  从redis 里取
     * @return  返回数据
     * @throws DataAccessException
     */
    User toUpdateMsg(String token)throws Exception;

    /**
     * 修改个人用户信息
     * @param bytes  将文件转成流的形式  进入service
     * @param user  传递参数
     * @throws Exception
     */
    void updateUserMsgById(byte[] bytes, User user)throws Exception;

    /**
     * //添加购物车  同一用户   同一个proSkuId   没有的话是新增   有的话更新数量
     * @param token  根据userId
     * @param proSkuId  proSkuId
     * @return  返回一条数据
     * @throws Exception
     */
    ShoppingCar getShoppinngCarByUserIdAndProSkuId(String token,  Integer proSkuId)throws Exception;
    /**
     * 判断加入购物车是否已经有数据    同一用户   同一个proSkuId  有的话  修改
     * @param shoppingCar
     * @throws Exception
     */
    void updateShoppingCarNumByUserIdAndProSkuId(ShoppingCar shoppingCar)throws Exception;

    /**
     * 添加购物车
     * @param shoppingCar  传递参数
     * @throws Exception
     */
    Integer insertShoppingCar(ShoppingCar shoppingCar, String token)throws Exception;
    /**
     *   根据用户id  去查询当前登录用户 的购物车信息
     * @param shoppingCar  传递参数 id、 redis 获取
     * @return  返回数据
     * @throws Exception
     */
    List<ShoppingCar> findShoppingCarById(ShoppingCar shoppingCar, String token)throws Exception;

    /**
     *购物车删除已选中的商品  批量删除
     * @param ids  参数
     * @throws Exception
     */
    void deleteShoppingCarByIds(@Param("ids") Integer [] ids)throws Exception;



    /**
     * 根据购物车id  去查询 一条数据
     * @return 返回 ShoppingCar
     * @throws Exception
     */
    ShoppingCar getShoppingCarById(Integer id)throws Exception;

    /**
     * 根据id 修改 购物车的数量
     * @param id
     * @throws Exception
     */
    void updateShoppingNumCarById( Integer id, Integer num)throws Exception;
    /**
     * 选中 立即结算  价格 邮费 商品个数 回显
     * @param ids  根据传来的ids进行查询
     * @return 返回数据
     * @throws Exception
     */
    List<ShoppingCar> getShoppingCarByIds(@Param("ids") Integer[] ids)throws Exception;


    /**
     *   根据前台传来的ids   去展示要确认的订单  进入确认订单页面
     * @param  ids  参数
     * @return  返回数据
     * @throws Exception
     */
    List<ShoppingCar> findShoppingCarByIds(Integer [] ids)throws Exception;
    /**
     * 在确认订单展示列表  回显登录人的地址列表集合
     * @param userId  根据userId  查询
     * @return
     * @throws Exception
     */
    List<Address> findAddressByUserIdToOrderList(Integer userId, String token)throws Exception;

    /**
     *   在确认订单展示列表   确认加入订单表  状态为待支付     减库存  删除对应购物车
     * @param order
     * @throws Exception
     */
    void insertOrder(List<Order> order, Integer[] ids) throws Exception;









    /**
     * 前台获取  proSkuId  从redis获取userId  查询是否已经点赞过    如果已经点赞过就不能再点赞
     * @param token 从redis获取userId
     * @param proSkuId
     * @return
     * @throws DataAccessException
     */
    Great getGreatByUserIdAndProSkuId(String token, Integer proSkuId)throws Exception;
    /**
     * 查询不空 说明已经点赞了 取消点赞  根据查询的id 进行删除
     * @param id  根据id 取消点赞
     * @throws Exception
     */
    void deleteGreatById(Integer id, Integer proId, Integer thumbNumber)throws Exception;

    /**
     * 保存点赞表     并 修改 商品 点赞数量
     *    @param token   redis  获取用户id
     *     @param proSkuId  商品属性id
     * @throws Exception
     */
    void addGreat( Integer id, String token,Integer proSkuId, Integer thumbNumber)throws Exception;


    /**
     * 根据productSku 中productId查询 product表中的卖家userId
     * @param id   根据id 查询商品表
     * @return
     * @throws Exception
     */
    Product getProductById(Integer id)throws Exception;

    /**
     * 直接去商品展示页面    查询好评率
     * @param productId
     * @return
     * @throws Exception
     */
    List<Common> findCommonAllByProductId(Integer productId)throws Exception;




















}
