package com.dj.mall.service.basedata.dictionary.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.mall.api.order.basedata.dictionary.DictionaryService;
import com.dj.mall.api.order.basedata.sku.SkuService;
import com.dj.mall.api.order.cmpt.RedisService;
import com.dj.mall.constant.SystemConstant;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.sku.entiy.ProductSkuGm;
import com.dj.mall.mapper.basedata.dictionary.DictionaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {



    @Autowired
    private SkuService skuService;
    @Autowired
    private RedisService redisService;

    /**
     * 用户展示
     * @param pageNo 当前页
     * @param dictionary 参数
     * @return 返回数据MAP
     * @throws Exception
     */
    @Override
    public Map<String, Object> findDictionaryAll(Integer pageNo, Dictionary dictionary) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Page<Dictionary> page = new Page<>(pageNo, SystemConstant.PAGESIZE_NUMBER_FOUR);
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        IPage<Dictionary> iPage = this.page(page, queryWrapper);
        map.put("pageNo", pageNo);
        map.put("list", iPage.getRecords());
        map.put("totalPage", iPage.getPages());
        return map;
    }

    /**
     *  新增  toList之前 准备下拉框
     * @return 返还数据
     * @throws Exception
     */
    @Override
    public List<Dictionary> findDictionaryBySystemPcode() throws Exception {
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_code", "SYSTEM");
        return this.list(queryWrapper);
    }
    /**
     * 去重
     * @param dictionaryName 字典名
     * @param code   编码
     * @return Dictionary  返还数据
     * @throws Exception
     */
    @Override
    public Dictionary findDictionaryToUniq(String dictionaryName, String code) throws Exception {
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        //条件查询
        if(!StringUtils.isEmpty(dictionaryName)){
            queryWrapper.eq("dictionary_name", dictionaryName);
        }
        if (!StringUtils.isEmpty(code)){
            queryWrapper.or();
            queryWrapper.eq("code", code);
        }
        return this.getOne(queryWrapper);
    }

    /**
     * 新增
     * @param dictionary  传递参数
     * @throws Exception
     */
    @Override
    public void insertDictionary(Dictionary dictionary) throws Exception {
        //将小写全部转成大写code
        dictionary.setCode(dictionary.getCode().toUpperCase());
        System.out.println(dictionary.getCode());
        //添加商品到djmall_product_sku_gm
        if(dictionary.getParentCode().equals("PRODUCT_TYPE")){
            ProductSkuGm productSkuGm = new ProductSkuGm();
            productSkuGm.setProductType(dictionary.getCode());
            skuService.insertProductSkuGmByDictionary(productSkuGm);
        }
        //保存redis 缓存
        redisService.pushHash(dictionary.getParentCode(), dictionary.getCode(), dictionary);
        //保存字典数据
        this.save(dictionary);
    }

    /**
     * 去修改
     * @param id  根据id 查询
     * @return   Dictionary 返回数据
     * @throws Exception
     */
    @Override
    public Dictionary toUpdate(Integer id) throws Exception {
        return this.getById(id);
    }

    /**
     * 修改
     * @param dictionary  传递参数
     * @throws Exception
     */
    @Override
    public void updateDictionary(Dictionary dictionary) throws Exception {
        //修改
        this.updateById(dictionary);
        //保存redis  根据parentCode  code  先删除已经保存在redis中的数据    然后再保存改后的
        //删除缓存
        redisService.delHash(dictionary.getParentCode(), dictionary.getCode());
        //再保存最新的数据到缓存
        redisService.pushHash(dictionary.getParentCode(), dictionary.getCode(),dictionary);
    }

    /**
     * 查询物流信息   parent_code  =  LOG
     * @return  返回List 泛型 Dictionary
     * @throws Exception
     */
    @Override
    public List<Dictionary> findDictionaryByLOG() throws Exception {
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_code", SystemConstant.LOG);
        return this.list(queryWrapper);
    }


}
