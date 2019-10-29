package com.dj.mall.mapper.basedata.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import com.dj.mall.domain.basedata.log.entiy.Log;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface LogMapper extends BaseMapper<Log> {
    /**
     * 运费展示
     * @return  返回list
     * @throws Exception
     */
    List<Log> findLogAndFreightAll() throws DataAccessException;

    /**
     * 添加运费
     * @param freight   传递参数
     * @throws Exception
     */
    void insertFreight(Freight freight) throws DataAccessException;


    /**
     * 去修改
     * @param id  根据id 去查询
     * @return  返回数据
     * @throws DataAccessException
     */
    Freight toUpdateFreightById(Integer id)throws DataAccessException;

    /**
     * 修改
     * @param freight  传参
     * @throws DataAccessException
     */
    void updateFreightById(Freight freight)throws DataAccessException;


    /**
     * 运费去重
     * @param dicCode   物流名称code
     * @param freightShow  运费
     * @return  返回数据
     * @throws DataAccessException
     */
    Log findLogIdAndFreightToUniq(@Param("dicCode") String dicCode, @Param("freightShow") String freightShow )throws DataAccessException;

    /**
     * 查询名称物流
     * @param id   根据id 查询
     * @return 返回数据
     * @throws DataAccessException
     */
    Dictionary findDicById(Integer id) throws DataAccessException;
}
