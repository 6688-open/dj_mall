package com.dj.mall.api.order.mall;


import com.dj.mall.domain.mall.entiy.Address;
import com.dj.mall.domain.mall.entiy.Area;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface AddressService /*extends IService<User>*/ {



    /**

     *  地址展示列表
     * @return  返回  page
     * @throws Exception
     */
    Map<String, Object> findAddressAll(Integer pageNo, String token)throws Exception;

    /**
     * 删除收货地址
     * @param id 根据id  进行删除收货地址
     * @throws Exception
     */
    void deleteAddress(Integer id)throws Exception;

    /**
     * 去修改收货地址   查询回显
     * @param id  根据id 查询 Address 实体类
     * @return  返回
     * @throws Exception
     */
    Address findAddressToUpdateById(Integer id)throws Exception;


    /**
     *  添加收货地址   三级联动
     * @return  返回集合
     * @throws Exception
     */
    List<Area> findAreaByParentId(Integer pId) throws Exception;
    /**
     * 根据省  市 县传来的id  去查询名字 保存地址列表  添加地址列表
     * @param pro  省 id
     * @param city 市  id
     * @param area 县 id
     * @return  返回集合
     * @throws DataAccessException
     */
    List<Area> findAreaNameById(Integer pro, Integer city, Integer area)throws Exception;

    /**
     * 新增地址列表
     * @param address  传递参数
     * @throws Exception
     */
    void add(Address address, String token)throws Exception;
    /**
     * 修改地址列表
     * @param address  传递参数
     * @throws Exception
     */
    void update(Address address)throws Exception;




}
