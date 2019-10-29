package com.dj.mall.admin.web.basedata.dictionary;

import com.dj.mall.api.order.basedata.dictionary.DictionaryService;
import com.dj.mall.common.ResultModel;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.dictionary.vo.DictionaryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dictionary/")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;


    /**
     * 字典数据展示
     * @param pageNo  当前页
     * @param dictionaryVo  传递参数
     * @return 返回数据 map
     */
    @GetMapping("list")
    public ResultModel list(Integer pageNo, DictionaryVo dictionaryVo){
        try {
            Dictionary dictionary = new Dictionary();
            BeanUtils.copyProperties(dictionaryVo, dictionary);
            return new ResultModel().success(dictionaryService.findDictionaryAll(pageNo, dictionary));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 添加信息去重
     * @param dictionaryName  字典名称
     * @param code  字典code
     * @return
     */
    @RequestMapping("uniq")
    public Boolean uniq(String dictionaryName, String code){
        try{
            Dictionary dictionary = dictionaryService.findDictionaryToUniq(dictionaryName, code);
            //判断是否有值
            if(null != dictionary){
                return false;//失败
            }
            return true;//成功
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 新增信息
     * @param dictionaryVo 传递参数
     * @return  返回map
     */
    @PostMapping("insert")
    public ResultModel insert(DictionaryVo dictionaryVo){
        try {
            Dictionary dictionary = new Dictionary();
            BeanUtils.copyProperties(dictionaryVo, dictionary);
            dictionaryService.insertDictionary(dictionary);
            return new ResultModel().success("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     * 修改
     * @param dictionaryVo  传递参数
     * @return  返回map
     */
    @PutMapping("update")
    public ResultModel update(DictionaryVo dictionaryVo){
        try {
            Dictionary dictionary = new Dictionary();
            BeanUtils.copyProperties(dictionaryVo, dictionary);
            dictionaryService.updateDictionary(dictionary);
            return new ResultModel().success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }



}
