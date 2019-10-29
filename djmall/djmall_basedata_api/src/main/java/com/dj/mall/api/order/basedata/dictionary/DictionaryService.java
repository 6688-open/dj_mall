package com.dj.mall.api.order.basedata.dictionary;

import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.user.entiy.User;

import java.util.List;
import java.util.Map;

public interface DictionaryService {

    /**
     * 用户展示
     * @param pageNo 当前页
     * @param dictionary 参数
     * @return 返回数据MAP
     * @throws Exception
     */
    Map<String, Object> findDictionaryAll(Integer pageNo, Dictionary dictionary)throws Exception;
    /**
     *  新增  toList之前 准备下拉框
     * @return 返还数据
     * @throws Exception
     */
    List<Dictionary> findDictionaryBySystemPcode()throws Exception;

    /**
     * 注册去重
     * @param dictionaryName 字典名
     * @param code   编码
     * @return Dictionary  返还数据
     * @throws Exception
     */
    Dictionary findDictionaryToUniq(String dictionaryName, String code) throws Exception;
    /**
     * 新增
     * @param dictionary  传递参数
     * @throws Exception
     */
    void insertDictionary(Dictionary dictionary) throws Exception;

    /**
     * 去修改
     * @param id  根据id 查询
     * @return   Dictionary 返回数据
     * @throws Exception
     */
    Dictionary toUpdate(Integer id)throws Exception;

    /**
     * 修改
     * @param dictionary  传递参数
     * @throws Exception
     */
    void updateDictionary(Dictionary dictionary)throws Exception;

    /**
     * 查询物流信息   parent_code  =  LOG
     * @return  返回List 泛型 Dictionary
     * @throws Exception
     */
    List<Dictionary> findDictionaryByLOG()throws Exception;

}
