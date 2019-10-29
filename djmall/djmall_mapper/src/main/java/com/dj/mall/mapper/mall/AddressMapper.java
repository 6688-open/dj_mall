package com.dj.mall.mapper.mall;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dj.mall.domain.mall.entiy.Address;
import com.dj.mall.domain.mall.entiy.Area;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AddressMapper extends BaseMapper<Address> {

    /**
     *  添加收货地址   三级联动
     * @return  返回 集合
     * @throws DataAccessException
     */
    List<Area> findAreaByParentId(Integer pId) throws DataAccessException;

    /**
     * 根据省  市 县传来的id  去查询名字 保存地址列表
     * @param pro  省 id
     * @param city 市  id
     * @param area 县 id
     * @return  返回集合
     * @throws DataAccessException
     */
    List<Area> findAreaNameById(@Param("pro") Integer pro,  @Param("city") Integer city, @Param("area") Integer area)throws DataAccessException;
}
