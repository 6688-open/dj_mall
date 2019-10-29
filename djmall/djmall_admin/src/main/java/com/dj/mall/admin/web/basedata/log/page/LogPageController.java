package com.dj.mall.admin.web.basedata.log.page;

import com.dj.mall.api.order.basedata.dictionary.DictionaryService;
import com.dj.mall.api.order.basedata.log.LogService;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/log/")
public class LogPageController {

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private LogService logService;

    /**
     * 跳转展示页面
     * @param map 回显下拉框物流信息
     * @return
     */
    @RequestMapping("toList")
    public String toList(ModelMap map){
        try {
            //查询字典数据  下拉框物流信息
            List<Dictionary> LOGList = dictionaryService.findDictionaryByLOG();
            map.put("LOGList", LOGList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "basedata/log/list";
    }

    /**
     * 去修改运费
     * @param id
     * @param map 回显
     * @return
     */
    @RequestMapping("toUpdate/{id}")
    public String toUpdate(@PathVariable("id") Integer id, ModelMap map){
        try {
            Freight freight = logService.toUpdateFreightById(id);
            map.put("freight", freight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "basedata/log/update";
    }

}
