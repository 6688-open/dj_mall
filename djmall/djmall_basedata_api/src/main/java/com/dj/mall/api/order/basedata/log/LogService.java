package com.dj.mall.api.order.basedata.log;


import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import com.dj.mall.domain.basedata.log.entiy.Log;

import java.util.List;

public interface LogService {
    /**
     * 展示
     * @return 返回list
     * @throws Exception
     */
    List<Log> findLogAndFreightAll() throws Exception;

    /**
     * 添加运费
     * @param freight 传递参数
     * @throws Exception
     */
    void insertFreight(Freight freight) throws Exception;

    /**
     * 去修改
     * @param id  根据id 查询
     * @return  Freight  返还数据
     * @throws Exception
     */
    Freight toUpdateFreightById(Integer id)throws Exception;

    /**
     * 修改
     * @param freight 传递参数
     * @throws Exception
     */
    void updateFreightById(Freight freight)throws Exception;

    /**
     * 运费去重
     * @param dicCode  快递名称
     * @param freightShow  运费
     * @return Log 返还数据
     * @throws Exception
     */
    Log findLogIdAndFreightToUniq( String dicCode, String freightShow )throws Exception;

    /**
     * 查询名称物流
     * @param id 根据id 查询
     * @return  Dictionary  返还数据
     * @throws Exception
     */
    Dictionary findDicById(Integer id) throws Exception;








}
