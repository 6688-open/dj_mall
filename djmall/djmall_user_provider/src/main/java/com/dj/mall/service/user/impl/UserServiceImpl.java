package com.dj.mall.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.mall.api.order.cmpt.EmailService;
import com.dj.mall.api.order.cmpt.RedisService;
import com.dj.mall.api.order.code.CodeService;
import com.dj.mall.api.order.user.UserService;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.mall.entiy.Address;
import com.dj.mall.domain.mall.entiy.Common;
import com.dj.mall.domain.mall.entiy.Order;
import com.dj.mall.domain.mall.entiy.ShoppingCar;
import com.dj.mall.domain.product.entiy.Great;
import com.dj.mall.domain.product.entiy.Product;
import com.dj.mall.domain.product.entiy.ProductSku;
import com.dj.mall.domain.product.entiy.ProductSolr;
import com.dj.mall.domain.user.entiy.User;
import com.dj.mall.mapper.mall.OrderMapper;
import com.dj.mall.mapper.product.ProductMapper;
import com.dj.mall.mapper.user.UserMapper;
import com.dj.mall.util.PasswordSecurityUtil;
import com.dj.mall.util.QiNiuUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.security.auth.login.AccountException;
import java.util.*;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private CodeService codeService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private HttpSolrClient httpSolrClient;
   /* @Autowired
    private ProductSkuService productSkuService;*/

   @Autowired
   private RabbitTemplate rabbitTemplate;

    /**
     * 用户登录
     * @param user 传递参数
     * @return User 返还数据
     * @throws Exception
     */
    @Override
    public User findUserByUsernameAndPassword(User user) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", user.getUsername()).or().eq("email", user.getUsername()).or().eq("username", user.getUsername());
        return this.getOne(queryWrapper);
    }

    /**
     * 注册去重
     * @param username 用户名
     * @param phone  手机号
     * @param email 邮箱
     * @return User 返还数据
     * @throws Exception
     */
    @Override
    public User findUserToUniq(String username, String phone, String email, String nickName) throws Exception {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //条件查询  用户名  手机号  邮箱  昵称
        if(!com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(username)){
            wrapper.eq("username", username);
        }
        if (!com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(phone)){
            wrapper.or();
            wrapper.eq("phone", phone);
        }
        if (!com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(email)){
            wrapper.or();
            wrapper.eq("email", email);
        }
        if (!com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(nickName)){
            wrapper.or();
            wrapper.eq("nick_name", nickName);
            wrapper.or();
            wrapper.eq("username", nickName);

        }
        return this.getOne(wrapper);
    }

    /**
     * 用户注册
     * @param user 传递参数
     * @throws Exception
     */
    @Override
    public Integer addUserToRegister(User user) throws Exception {
        //注册
        this.save(user);

        //商户发邮件
        if (user.getLever().equals(SystemConstant.LEVER_NUMBER_ONE)) {
            //rabbitMQ实现 加入队列
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", user.getId());
            jsonObject.put("userEmail", user.getEmail());
            rabbitTemplate.convertAndSend("sendEmail", jsonObject.toString());
          /*  String url = "http://127.0.0.1:8090/admin/user/updateStatus?id="+user.getId();
            String to = user.getEmail();
            String subject = "DJ商城";
            String text = "您的激活链接是"+"<p><a href="+url+">点我激活</a></p>";
            //emailService.sendMailHTML(to, subject, text);
            System.out.println("您的激活链接是"+url);*/
        }
        return user.getId();
    }


    /**
     * 商户邮箱激活 修改状态
     * @param id  根据 id 激活修改状态
     * @throws Exception
     */
    @Override
    public void updateStatusToUse(Integer id) throws Exception {
        User user = new User();
        user.setId(id);
        user.setStatus(SystemConstant.STATUS_NUMBER_ONE);
        this.updateById(user);
    }

    /**
     * 忘记密码 获取验手机证码
     * @param user  传递参数
     * @throws Exception
     */
    @Override
    public void saveAndRemove(User user) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",user.getPhone());
        User one = this.getOne(queryWrapper);
        if(null == one){
            throw new AccountException("手机不存在");
        }
        //设置验证码
        Random random = new Random();
        String result="";
        for(int i=0;i<6;i++){
            result+=random.nextInt(10);
        }
        //有效时间设置
       /* Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 10);// 今天+2 分钟
        Date date = c.getTime();

        Code code = new Code();
        code.setCode(result);
        code.setPhone(one.getPhone());
        code.setType(SystemConstant.TYPE_NUMBER); //    1 获取手机短信 忘记密码          type =2
        code.setTime(date);*/

      /*  QueryWrapper<Code> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("phone",one.getPhone());
        queryWrapper1.eq("type",SystemConstant.TYPE_NUMBER);//类型为1*/
        //先删掉之前发送的短信验证码记录
        //codeService.remove(queryWrapper1);
        //保存最新的
        // codeService.save(code);


        //设置时间缓存  验证码有效期   60秒自动位空
        /* redisService.set(one.getPhone(),result,60);*/



        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", result);
        //过期时间 1 分钟  60000   毫秒
        jsonObject.put("timeOut", new Date().getTime() + SystemConstant.ONE_MINUETS_TIME);

        JSONObject hash = redisService.getHash(one.getPhone(), user.getCodeType());
        // 如果不是第一次发送验证码的话进判断
        if(hash != null ){
            // 如果缓存中的数据次数 大于 3 的话
            if(Integer.valueOf((hash.getString("number"))) ==  SystemConstant.NUMBER_THREE){
                throw  new AccountException("您今日的发送验证发的次数已经用完请明日在用");
            } else {
                // 存入次数  小于2 的话 +1 在存入缓存中
                jsonObject.put("number", Integer.valueOf(hash.getString("number"))  + SystemConstant.NUMBER_ONE);
            }
        }
        if(hash == null ){
            // 存入次数
            jsonObject.put("number", SystemConstant.NUMBER_ONE);
        }
        // 将 手机号  超时时间  验证码类型 存入到redis中      24小时毫秒值  864000
        redisService.pushHash(one.getPhone(), user.getCodeType(), jsonObject, (SystemConstant.ONE_HOUR - new Date().getMinutes()));
        System.out.println(jsonObject);




        //发送邮件
        //SendMsgUtils.sendPhoneMsgAndTime(one.getPhone(),result,"10",136560);
        System.out.println("验证码是"+result);
        map.put("code",result);
    }

    /**
     * 忘记密码  发送邮箱
     * @param user 传递参数
     * @param code  判断验证码 是否一致
     * @throws Exception
     */
    @Override
    public void updateResetPasswordAndSendEmail(User user, String code, String pwd) throws Exception {
        //判断手机验证码是否正确
       /* QueryWrapper<Code> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", user.getPhone());
        Code code1 = codeService.getOne(queryWrapper);
        if(!code.equals(code1.getCode())){
            throw new AccountException("手机验证码不正确");
        }*/

        //获取之前设置的缓存验证码  判断手机验证码是否正确
       /* Object code1 = redisService.get(user.getPhone());
        System.out.println(code1);
        if(code1 == null){ //缓存为空 说明失效
            throw new AccountException("验证码失效");
        }
        if(!code.equals(code1)){//判断前台输入的验证码  和 从缓存取的验证码是否一致
            throw new AccountException("验证码不正确");
        }*/

        JSONObject hash = redisService.getHash(user.getPhone(), user.getCodeType());
        String code1 = hash.getString("code");
        System.out.println(code + hash.getString("timeOut"));

        long time = new Date().getTime();
        if (Long.valueOf(hash.getString("timeOut")) < time){
            throw new AccountException("验证码超时间请重新发送");
        }
        if(!code.equals(code1)){//前台传来的验证码 和 从redis中取的是否一致
            throw new AccountException("验证码不正确");
        }

        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("phone", user.getPhone());
        User user1 = this.getOne(queryWrapper1);
        //前台加密 修改加盐
        String salt = PasswordSecurityUtil.generateSalt();
        String overPassword = PasswordSecurityUtil.generatePassword(user.getPassword(), salt);
        user1.setPassword(overPassword);
        user1.setSalt(salt);
        this.updateById(user1);

        //发送邮箱  给用户是没有加盐 没有加密的数字 pwd
        String to = user1.getEmail();
        String subject = "DJ商城";
        String text = "您修改的密码是"+pwd;
        //emailService.sendMail(to, subject, text);
        System.out.println("您修改的密码是"+pwd);

    }

    /**
     * 用户展示
     * @param pageNo  当前页
     * @param user  传递参数
     * @return 返还数据
     * @throws Exception
     */
    @Override
    public Map<String, Object> findUserAll(Integer pageNo, User user) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Page<User> page = new Page<>(pageNo, SystemConstant.PAGESIZE_NUMBER);
        QueryWrapper<User>  queryWrapper = new QueryWrapper<>();
        if( null != user.getSex()) {
            queryWrapper.eq("sex", user.getSex());
        }
        if( null != user.getLever()) {
            queryWrapper.eq("lever", user.getLever());
        }
        if( null != user.getStatus()) {
            queryWrapper.eq("status", user.getStatus());
        }
        if(!StringUtils.isEmpty(user.getUsername())   ) {
            queryWrapper.and(i -> i.like("username", user.getUsername()).or().like("phone", user.getUsername()).or().like("email",user.getUsername()));
        }
        queryWrapper.orderByDesc("id");
        IPage<User> iPage = this.page(page, queryWrapper);
        map.put("pageNo", pageNo);
        map.put("list", iPage.getRecords());
        map.put("totalPage", iPage.getPages());
        return map;
    }

    /**
     * 批量删
     * @param ids 根据 ids 删除
     * @throws Exception
     */
    @Override
    public void delMoreUser(Integer[] ids) throws Exception {
        List<Integer> resultList= new ArrayList<>(Arrays.asList(ids));
        this.removeByIds(resultList);
    }

    /**
     * 批量激活
     * @param ids 根据 ids 激活
     * @throws Exception
     */
    @Override
    public void updateStatus(Integer[] ids) throws Exception {
        //查询是否未激活
        String userStatusName = "";
        for (Integer id : ids) {
            User user = this.getById(id);
            if(user.getStatus().equals(SystemConstant.STATUS_NUMBER_ONE)){
                userStatusName += user.getUsername()+",";
            }
        }
        //将已经激活的用户的姓名拼接  如果不为空说明有已经被激活的  抛出异常
        if(userStatusName != ""){
            throw new AccountException("用户"+userStatusName +"已经被激活");
        }
        //批量修改
        List<User> list = new ArrayList<>();
        for (Integer id : ids) {
            User user = new User();
            user.setId(id).setStatus(SystemConstant.STATUS_NUMBER_ONE);
            list.add(user);
        }
        this.updateBatchById(list);
    }

    /**
     * 修改用户
     * @param id  根据id 修改
     * @throws Exception
     */
    @Override
    public User toUpdateUser(Integer id) throws Exception {
        return this.getById(id);
    }

    /**
     * 修改用户
     * @param user 传递参数
     * @throws Exception
     */
    @Override
    public void updateUser(User user) throws Exception {
        this.updateById(user);
    }

    /**
     * 重置密码 发送邮件
     * @param id  根据id 重置  发送邮箱
     * @throws Exception
     */
    @Override
    public void updatePasswordAndSendEmail(Integer id) throws Exception {
        //设置验证码
        Random random = new Random();
        String result="";
        for(int i=0;i<6;i++){
            result+=random.nextInt(SystemConstant.TEN);
        }
        //重置密码
        User user = new User();
        user.setId(id);
        user.setIsReset(SystemConstant.ACTIVATE_ONE);
        user.setRestPassword(result);
        this.updateById(user);
        User user1 = this.getById(id);

        //发送邮箱  给用户是没有加盐 没有加密的数字 pwd
        String to = user1.getEmail();
        String subject = "DJ商城";
        String text = "您修改的密码是"+result;
        emailService.sendMail(to, subject, text);
        System.out.println("您修改的密码是"+result);

    }

    /**
     * 当使用重置密码登陆时  去修改密码
     * @param user 传递参数
     * @throws Exception
     */
    @Override
    public void updateAndRestPasswordUpdateIsReset(User user) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", user.getId());
        User user1 = this.getOne(queryWrapper);

        //前台加密 修改加盐    改变首次重置状态
        String salt = PasswordSecurityUtil.generateSalt();
        String overPassword = PasswordSecurityUtil.generatePassword(user.getPassword(), salt);
        user1.setPassword(overPassword);
        user1.setSalt(salt);
        user1.setIsReset(SystemConstant.ACTIVATE_TWO);
        user1.setRestPassword("");
        this.updateById(user1);

    }












    /**
     * 手机号码登录验证  前台输入的验证码和 之前存的redis里的进行比较  一天3次  时间超时 验证
     * @param user
     * @param phoneCode
     * @return
     */
    @Override
    public void findUserByPhoneAndRedisPhoneCode(User user, String phoneCode) throws Exception {
        JSONObject hash = redisService.getHash(user.getPhone(), user.getCodeType());
        String code = hash.getString("code");
        System.out.println(code + hash.getString("timeOut"));

        long time = new Date().getTime();
        if (Long.valueOf(hash.getString("timeOut")) < time){
            throw new AccountException("验证码超时间请重新发送");
        }
        if(!code.equals(phoneCode)){//前台传来的验证码 和 从redis中取的是否一致
            throw new AccountException("验证码不正确");
        }
    }




    /**
     * 通过solr 查询商品展示列表
     * @param pageNo  参数
     * @param product  传递参数
     * @return  返回map
     * @throws Exception
     */
    @Override
    public Map<String, Object> findProductBySolr(Integer pageNo, Product product) throws Exception {
        Map<String, Object> map  = new HashMap<>();
        //new 一个SolrQuery
        SolrQuery query = new SolrQuery();
        //查询条件
        query.setQuery("*:*");


        //设置高亮
        if(!StringUtils.isEmpty(product.getProductName())){
            query.setQuery("product_keywords:"+product.getProductName()); //必须为查询条件
            //开启高亮
            query.setHighlight(true);
            //指定高亮字段
            query.addHighlightField("product_describe");
            query.addHighlightField("dictionaryNameShow");
            query.addHighlightField("product_name");
            query.addHighlightField("skuAttrValuesNames");
            //高亮单词的前缀
            query.setHighlightSimplePre("<span style='color:red; font-size:16px'><b>");
            //高亮单词的后缀
            query.setHighlightSimplePost("</b></span>");
        }



        //过滤条件 关键字查询
        if(!StringUtils.isEmpty(product.getProductName())){
            query.addFilterQuery("product_keywords:"+product.getProductName());
        }
        if(product.getStartPrice() != null && product.getEndPrice() != null ){
            query.addFilterQuery("priceShow:[" +product.getStartPrice()+ " TO "+product.getEndPrice()+"]");
        }
        if(product.getStartPrice() != null && product.getEndPrice() == null  ){
            query.addFilterQuery("priceShow:[" +product.getStartPrice()+ " TO *]");
        }
        if(product.getStartPrice() == null && product.getEndPrice() != null  ){
            query.addFilterQuery("priceShow:[*  TO  "+product.getEndPrice()+"]");
        }
        //分类查询   dictionary_code: 1 OR dictionary_code: 2  大写  拼接OR
        String str = "";
        if(product.getDictionaryCodeArr() != null && product.getDictionaryCodeArr().length>0){
            for (String s : product.getDictionaryCodeArr()) {
                str += " "+ "dictionary_code:" +s+ " OR ";
            }
            str=str.substring(SystemConstant.NUMBER_ONE,str.length()-SystemConstant.NUMBER_THREE);//1  --  3
            query.addFilterQuery(str);
        }



        //展示默认的一条
        query.addFilterQuery("sku_is_default:1");


       //排序条件  多条件  solr必须为double
        List<SolrQuery.SortClause> sortList = new ArrayList<>();
        sortList.add(new SolrQuery.SortClause("priceShow",SolrQuery.ORDER.asc));
        //根据多字段排序
        query.setSorts(sortList);
        //根据一个字段排序
        //query.setSort("priceShow", SolrQuery.ORDER.asc);

        //分页设置   4
        Integer pageSize = SystemConstant.PAGESIZE_NUMBER_FOUR;
        query.setStart((pageNo-SystemConstant.NUMBER_ONE)*pageSize); //开始记录数
        query.setRows(pageSize);  //总条数

        QueryResponse queryResponse = httpSolrClient.query(query);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        // 总条数
        Long totalPageNum =  solrDocumentList.getNumFound();
        Double totalPage =Math.ceil(totalPageNum * 1.0 / pageSize * 1.0) ;
        //获取数据
        List<ProductSolr> list = queryResponse.getBeans(ProductSolr.class);


        //获取高亮字段
        Map<String, Map<String, List<String>>> hlList= queryResponse.getHighlighting();
       if(hlList != null && hlList.size()> SystemConstant.NUMBER_ZERO ){
           //获取的所有数据集合   便利赋值
           Integer i = SystemConstant.NUMBER_ZERO;
           for (SolrDocument solrDocument : solrDocumentList) {
               String proSkuId = solrDocument.getFieldValue("id").toString();

               Map<String, List<String>> listMap = hlList.get(proSkuId);
               String productName = list.get(i).getProductName();
               String dictionaryNameShow = list.get(i).getDictionaryNameShow();
               String productDescribe = list.get(i).getProductDescribe();
               String skuAttrValuesNames = list.get(i).getSkuAttrValuesNames();
               if(listMap.size() > SystemConstant.NUMBER_ZERO){
                   List<String> productNameList = listMap.get("product_name");
                   List<String> dictionaryNameShowList = listMap.get("dictionaryNameShow");
                   List<String> productDescribeList = listMap.get("product_describe");
                   List<String> skuAttrValuesNamesList = listMap.get("skuAttrValuesNames");
                   if(productNameList != null && productNameList.size() > SystemConstant.NUMBER_ZERO ){
                       productName =  productNameList.get(SystemConstant.NUMBER_ZERO);
                   }
                   if(dictionaryNameShowList != null && dictionaryNameShowList.size() > SystemConstant.NUMBER_ZERO){
                       dictionaryNameShow =  dictionaryNameShowList.get(SystemConstant.NUMBER_ZERO);
                   }
                   if(productDescribeList != null && productDescribeList.size() > SystemConstant.NUMBER_ZERO){
                       productDescribe =  productDescribeList.get(SystemConstant.NUMBER_ZERO);
                   }
                   if(skuAttrValuesNamesList != null && skuAttrValuesNamesList.size() > SystemConstant.NUMBER_ZERO){
                       skuAttrValuesNames =  skuAttrValuesNamesList.get(SystemConstant.NUMBER_ZERO);
                   }

               }
               list.get(i).setProductName(productName);
               list.get(i).setDictionaryNameShow(dictionaryNameShow);
               list.get(i).setProductDescribe(productDescribe);
               list.get(i).setSkuAttrValuesNames(skuAttrValuesNames);
               i++;


           }
       }

        map.put("list", list);
        map.put("totalPage", totalPage);
        map.put("pageNo", pageNo);

        return map;
    }



    /**
     * 商城展示列表
     * @param page  分页page对象
     * @param product  模糊查对象
     * @return  返回实体类
     * @throws Exception
     */
    @Override
    public Page<Product> findProductAndSKUAll(Page page, Product product) throws Exception {
        return userMapper.findProductAndSKUAll(page, product);
    }
    /**
     *  商品详情
     * @param id   根据id 查询
     * @return  返回 实体类 Product
     * @throws Exception
     */
    @Override
    public Product findProductMsgById(Integer id) throws Exception {
        return userMapper.findProductMsgById(id);
    }
    /**
     * 根据商品id 查询对应所有的sku 属性值
     * @param productId  根据商品id  查询
     * @return  返回ProductSku 数据
     * @throws Exception
     */
    @Override
    public List<ProductSku> findProductSkuByProductId(Integer productId) throws Exception {
        return userMapper.findProductSkuByProductId(productId);
    }
    /**
     *  根据 id 查询数据   查询价格   不同sku 对应不同的价格   点击单选按钮 价格改变
     * @param id  根据id 查询
     * @return  返回数据
     * @throws Exception
     */
    @Override
    public ProductSku findProductSkuById(Integer id) throws Exception {
        return userMapper.findProductSkuById(id);
    }

    /**
     * 去查询用户信息  从redis 里取
     * @return  返回数据
     * @throws Exception
     */
    @Override
    public User toUpdateMsg(String token) throws Exception {
        User user = redisService.get(token);
        return userMapper.toUpdateMsg(user.getId());

    }
    /**
     * 修改个人用户信息
     * @param bytes  将文件转成流的形式  进入service
     * @param user  传递参数
     * @throws Exception
     */
    @Override
    public void updateUserMsgById(byte[] bytes, User user) throws Exception {
        if(null != bytes) {
            String uuName = UUID.randomUUID().toString();
            QiNiuUtils.put64image(bytes, uuName);
            String finalName = SystemConstant.YU_MING + uuName;
            user.setHeadImg(finalName);
        }
        this.updateById(user);
    }
    /**
     * //添加购物车  同一用户   同一个proSkuId   没有的话是新增   有的话更新数量
     * @param token  根据userId
     * @param proSkuId  proSkuId
     * @return  返回一条数据
     * @throws Exception
     */
    @Override
    public ShoppingCar getShoppinngCarByUserIdAndProSkuId(String token, Integer proSkuId) throws DataAccessException {
        //获取redis中的 user  id
        User user = redisService.get(token);
        return userMapper.getShoppinngCarByUserIdAndProSkuId(user.getId(),proSkuId);
    }
    /**
     * 判断加入购物车是否已经有数据    同一用户   同一个proSkuId  有的话  修改
     * @param shoppingCar
     * @throws Exception
     */
    @Override
    public void updateShoppingCarNumByUserIdAndProSkuId(ShoppingCar shoppingCar) throws Exception {
        userMapper.updateShoppingCarNumByUserIdAndProSkuId(shoppingCar);
    }

    /**
     * 添加购物车
     * @param shoppingCar  传递参数
     * @throws Exception
     */
    @Override
    public Integer insertShoppingCar(ShoppingCar shoppingCar, String token) throws Exception {
        //获取redis中的 user  id
        User user = redisService.get(token);
        shoppingCar.setUserId(user.getId());
        userMapper.insertShoppingCar(shoppingCar);
        return shoppingCar.getId();
    }
    /**
     *   根据用户id  去查询当前登录用户 的购物车信息
     * @param shoppingCar  传递参数 id、 redis 获取
     * @return  返回数据
     * @throws Exception
     */
    @Override
    public List<ShoppingCar> findShoppingCarById(ShoppingCar shoppingCar, String token) throws Exception {
        //获取redis中的 user  id
        User user = redisService.get(token);
        shoppingCar.setUserId(user.getId());
        return userMapper.findShoppingCarById(shoppingCar);
    }
    /**
     *购物车删除已选中的商品  批量删除
     * @param ids  参数
     * @throws Exception
     */
    @Override
    public void deleteShoppingCarByIds(Integer[] ids) throws Exception {
        userMapper.deleteShoppingCarByIds(ids);
    }
    /**
     * 根据购物车id  去查询 一条数据
     * @return 返回 ShoppingCar
     * @throws Exception
     */
    @Override
    public ShoppingCar getShoppingCarById(Integer id) throws Exception {
        return userMapper.getShoppingCarById(id);
    }
    /**
     * 根据id 修改 购物车的数量
     * @param id
     * @throws Exception
     */
    @Override
    public void updateShoppingNumCarById(Integer id, Integer num) throws Exception {
        userMapper.updateShoppingNumCarById(id, num);
    }
    /**
     * 选中 立即结算  价格 邮费 商品个数 回显
     * @param ids  根据传来的ids进行查询
     * @return 返回数据
     * @throws Exception
     */
    @Override
    public List<ShoppingCar> getShoppingCarByIds(Integer[] ids) throws Exception {
        return userMapper.getShoppingCarByIds(ids);
    }
    /**
     *   根据前台传来的ids   去展示要确认的订单  进入确认订单页面
     * @param  ids  参数
     * @return  返回数据
     * @throws Exception
     */
    @Override
    public List<ShoppingCar> findShoppingCarByIds(Integer[] ids) throws Exception {
        return userMapper.findShoppingCarByIds(ids);
    }
    /**
     * 在确认订单展示列表  回显登录人的地址列表集合
     * @param userId  根据userId  查询
     * @return
     * @throws Exception
     */
    @Override
    public List<Address> findAddressByUserIdToOrderList(Integer userId, String token) throws Exception {
        //获取redis中的 user  id
        User user = redisService.get(token);
        return userMapper.findAddressByUserIdToOrderList(user.getId());
    }
    /**
     *  在确认订单展示列表   确认加入订单表  状态为待支付     减库存  删除对应购物车
     * @param order
     * @throws Exception
     */
    @Override
    public void insertOrder(List<Order> order, Integer[] ids) throws Exception {
        //将确认订单加入redis   查询redis 45分钟自动取消订单
        //存子集订单号  作为第二个key  i 对象
        order.forEach(i ->{
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MINUTE, SystemConstant.NUMBER_FOUR_FIVE);// 今天+45 分钟
            Date date = c.getTime();
            i.setRedisTime(date);
            redisService.pushHash("ORDER", i.getOrderSonNum(),i);
        });
        //添加订单表
        userMapper.insertOrder(order);
        //修改库存
        List<ShoppingCar> shoppingCarByIds = userMapper.findShoppingCarByIds(ids);
        for (ShoppingCar shoppingCar : shoppingCarByIds) {
            orderMapper.updateProductSkuById(shoppingCar.getProSkuId(), shoppingCar.getNum());
        }
        //删除购物车
        userMapper.deleteShoppingCarByIds(ids);



    }




    /**
     * 查询所有订单展示  商户  管理员可见
     * @return
     * @throws DataAccessException
     */
    @Override
    public List<Order> findOrderAll(Integer lever, Integer sellerId) throws Exception {
        return userMapper.findOrderAll(lever, sellerId);
    }
    /**
     * 卖家去发货 将待发货2   修改成已发货，待收货状态  3
     * @param orderSonNum 子集订单
     * @param message  已发货
     * @throws Exception
     */
    @Override
    public void updateMessage(String orderSonNum, Integer message) throws Exception {
        userMapper.updateMessage(orderSonNum, message);
    }







    /**
     * 前台获取  proSkuId  从redis获取userId  查询是否已经点赞过    如果已经点赞过就不能再点赞
     * @param token 从redis获取userId
     * @param proSkuId
     * @return
     * @throws DataAccessException
     */
    @Override
    public Great getGreatByUserIdAndProSkuId(String token, Integer proSkuId) throws Exception {
        //获取redis中的 user  id
        User user = redisService.get(token);
        return userMapper.getGreatByUserIdAndProSkuId(user.getId(), proSkuId);
    }
    /**
     * 查询不空 说明已经点赞了 取消点赞  根据查询的id 进行删除
     * @param id  根据id 取消点赞
     * @throws Exception
     */
    @Override
    public void deleteGreatById(Integer id, Integer proId, Integer thumbNumber) throws Exception {
        //删除点赞这条数据
        userMapper.deleteGreatById(id);
        //修改商品表 点赞数量 条件 前台传来的点赞量是两次   减去一次   再减去上次添加的点赞量
        userMapper.updateProductThumbById(proId, thumbNumber- SystemConstant.TWO);
    }

    /**
     * 保存点赞表
     *    @param token  用户id
     *     @param proSkuId  商品属性id
     * @throws Exception
     */
    @Override
    public void addGreat(Integer id, String token, Integer proSkuId, Integer thumbNumber) throws Exception {
        //获取redis中的 user  id
        User user = redisService.get(token);
        //保存点赞表
        userMapper.addGreat(user.getId(), proSkuId);
        //修改商品表 点赞数量 条件
        userMapper.updateProductThumbById(id, thumbNumber);
    }


    /**
     * 根据productSku 中productId查询 product表中的卖家userId
     * @param id   根据id 查询商品表
     * @return
     * @throws Exception
     */
    @Override
    public Product getProductById(Integer id) throws Exception {
        return userMapper.getProductById(id);
    }
    /**
     * 直接去商品展示页面    查询好评率
     * @param productId
     * @return
     * @throws Exception
     */
    @Override
    public List<Common> findCommonAllByProductId(Integer productId) throws Exception {
        return userMapper.findCommonAllByProductId(productId);
    }


}
