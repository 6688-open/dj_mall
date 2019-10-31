package com.dj.mall.service.mall.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.mall.api.order.cmpt.RedisService;
import com.dj.mall.api.order.mall.AddressService;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.mall.entiy.Address;
import com.dj.mall.domain.mall.entiy.Area;
import com.dj.mall.domain.user.entiy.User;
import com.dj.mall.mapper.mall.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private AddressMapper addressMapper;

    /**
     *  地址展示列表
     * @return  返回  page
     * @throws Exception
     */
    @Override
    public Map<String, Object> findAddressAll(Integer pageNo, String token) throws Exception {
        //从redis中获取用户的id  谁登录的  只能看到自己的收货地址
        User user = redisService.get(token);
        Map<String, Object> map = new HashMap<String, Object>();
        Page<Address> page = new Page<>(pageNo, SystemConstant.PAGESIZE_NUMBER);
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", user.getId());
        queryWrapper.orderByDesc("id");
        IPage<Address> iPage = this.page(page, queryWrapper);
        map.put("pageNo", pageNo);
        map.put("list", iPage.getRecords());
        map.put("totalPage", iPage.getPages());
        return map;
    }
    /**
     * 删除收货地址
     * @param id 根据id  进行删除收货地址
     * @throws Exception
     */
    @Override
    public void deleteAddress(Integer id) throws Exception {
        this.removeById(id);
    }
    /**
     * 去修改收货地址   查询回显
     * @param id  根据id 查询 Address 实体类
     * @return  返回
     * @throws Exception
     */
    @Override
    public Address findAddressToUpdateById(Integer id) throws Exception {
        return this.getById(id);
    }
    /**
     *  添加收货地址   三级联动
     * @return  返回集合
     * @throws Exception
     */
    @Override
    public List<Area> findAreaByParentId(Integer pId) throws Exception {
        return addressMapper.findAreaByParentId(pId);
    }
    /**
     * 根据省  市 县传来的id  去查询名字 保存地址列表
     * @param pro  省 id
     * @param city 市  id
     * @param area 县 id
     * @return  返回集合
     * @throws Exception
     */
    @Override
    public List<Area> findAreaNameById(Integer pro, Integer city, Integer area) throws Exception {
        return addressMapper.findAreaNameById(pro, city, area);
    }
    /**
     * 新增地址列表
     * @param address  传递参数
     * @throws Exception
     */
    @Override
    public void add(Address address, String token) throws Exception {
        //从redis中获取用户的id  谁登录的  只能看到自己的收货地址
        User user = redisService.get(token);
        //赋值  登陆人的id
        address.setUserId(user.getId());
        this.save(address);
    }
    /**
     * 修改地址列表
     * @param address  传递参数
     * @throws Exception
     */
    @Override
    public void update(Address address) throws Exception {
        this.updateById(address);
    }


}
