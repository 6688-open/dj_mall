package com.dj.mall.service.basedata.log.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.mall.api.order.basedata.dictionary.DictionaryService;
import com.dj.mall.api.order.basedata.log.LogService;
import com.dj.mall.api.order.cmpt.RedisService;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import com.dj.mall.domain.basedata.log.entiy.Log;
import com.dj.mall.mapper.basedata.log.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private RedisService redisService;

    /**
     * 展示
     * @return 返回list
     * @throws Exception
     */
    @Override
    public List<Log> findLogAndFreightAll() throws Exception {
        return logMapper.findLogAndFreightAll();
    }

    /**
     * 新增
     * @param freight 传递参数
     * @throws Exception
     */
    @Override
    public void insertFreight(Freight freight) throws Exception {
        logMapper.insertFreight(freight);
    }

    /**
     * 去修改
     * @param id  根据id 查询
     * @return  Freight  返还数据
     * @throws Exception
     */
    @Override
    public Freight toUpdateFreightById(Integer id) throws Exception {
        return logMapper.toUpdateFreightById(id);
    }

    /**
     * 修改
     * @param freight 传递参数
     * @throws Exception
     */
    @Override
    public void updateFreightById(Freight freight) throws Exception {
        logMapper.updateFreightById(freight);
    }

    /**
     * 运费去重
     * @param dicCode  根据 dicCode  去重
     * @param freightShow 运费
     * @return 返还数据
     * @throws Exception
     */
    @Override
    public Log findLogIdAndFreightToUniq(String dicCode, String freightShow) throws Exception {
        return logMapper.findLogIdAndFreightToUniq(dicCode, freightShow);
    }
    /**
     * 查询名称物流
     * @param id 根据id 查询
     * @return  Dictionary  返还数据
     * @throws Exception
     */
    @Override
    public Dictionary findDicById(Integer id) throws Exception {
        return logMapper.findDicById(id);
    }


}
