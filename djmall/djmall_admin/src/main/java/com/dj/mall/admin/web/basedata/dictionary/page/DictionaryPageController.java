package com.dj.mall.admin.web.basedata.dictionary.page;

import com.dj.mall.api.order.basedata.dictionary.DictionaryService;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dictionary/")
public class DictionaryPageController {
    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 去展示之前 下拉框显示分类上级
     * @param map  返回的数据
     * @return
     */
    @RequestMapping("toList")
    public String toList(ModelMap map){
        try {
            List<Dictionary> dictionaryList = dictionaryService.findDictionaryBySystemPcode();
            map.put("dictionaryList", dictionaryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "basedata/dictionary/list";
    }

    /**
     * 去修改
     * @param id  根据id 查询
     * @param map 返回数据回显
     * @return
     */
    @RequestMapping("toUpdate/{id}")
    public String toUpdate(@PathVariable("id") Integer id, ModelMap map){
        try {
            Dictionary dictionary = dictionaryService.toUpdate(id);
            map.put("dictionary", dictionary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "basedata/dictionary/update";
    }
}
