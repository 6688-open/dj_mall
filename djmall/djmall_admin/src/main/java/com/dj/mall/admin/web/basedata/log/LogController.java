package com.dj.mall.admin.web.basedata.log;

import com.dj.mall.api.order.basedata.log.LogService;
import com.dj.mall.common.ResultModel;
import com.dj.mall.domain.basedata.dictionary.entiy.Dictionary;
import com.dj.mall.domain.basedata.log.entiy.Freight;
import com.dj.mall.domain.basedata.log.entiy.Log;
import com.dj.mall.domain.basedata.log.vo.LogVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log/")
public class LogController {

    @Autowired
    private LogService logService;


    /**
     * 字典数据展示
     * @param logVo 接受参数
     * @return
     */
    @GetMapping("list")
    public ResultModel list( LogVo logVo){
        try {
            Map<String, Object>  map  =  new HashMap<>();
            Log log = new Log();
            BeanUtils.copyProperties(logVo, log);
            List<Log> logList = logService.findLogAndFreightAll();
            map.put("logList", logList);
            return new ResultModel().success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }
    /**
     * 添加
     * @param freight  传递参数
     * @return
     */
    @RequestMapping("insert")
    public ResultModel insert(Freight freight){
        try {
            Dictionary dicById = logService.findDicById(freight.getLogId());
            //判断去重
            Log freightToUniq = logService.findLogIdAndFreightToUniq(dicById.getDictionaryName(), freight.getFreight());
            if(freightToUniq != null){
                return new ResultModel().error("该物流运费已存在");
            }
            //新增
            logService.insertFreight(freight);
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     *  修改
     * @param freight  接收参数
     * @return
     */
    @RequestMapping("update")
    public ResultModel update(Freight freight){
        try {
            Dictionary dicById = logService.findDicById(freight.getLogId());
            //判断去重
            Log freightToUniq = logService.findLogIdAndFreightToUniq(dicById.getDictionaryName(), freight.getFreight());
            if(null != freightToUniq){
                return new ResultModel().error("该物流运费已存在");
            }
            //修改
            logService.updateFreightById(freight);
            return new ResultModel().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }

    }





}
