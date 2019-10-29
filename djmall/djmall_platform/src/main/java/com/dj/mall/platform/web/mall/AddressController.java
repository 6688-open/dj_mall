package com.dj.mall.platform.web.mall;

import com.dj.mall.api.order.mall.AddressService;
import com.dj.mall.common.ResultModel;
import com.dj.mall.domain.mall.entiy.Address;
import com.dj.mall.domain.mall.entiy.Area;
import com.dj.mall.domain.mall.vo.AddressVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     *  个人地址展示列表
     * @return
     */
    @RequestMapping("list")
    public ResultModel list(Integer pageNo, String token){
        try {
            return new ResultModel().success(addressService.findAddressAll(pageNo, token));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     *删除个人地址
     * @return   带着token   根据id 进行删除
     */
    @RequestMapping("delAddress")
    public ResultModel delAddress(Integer id, String token){
        try {
            addressService.deleteAddress(id);
            return new ResultModel().success("");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel().error("异常"+e.getMessage());
        }
    }

    /**
     *  新增地址列表时 去回显城市数据  三级联动
     * @param pId  根据pid查询
     * @return
     * sql 写法   没有用plus
     */
    @RequestMapping("getCity/{pId}")
    public ResultModel getCity(@PathVariable("pId")Integer pId) {
        try {
           //根据前台 传来的pid   去查询所对应连动数据
            List<Area> areaList = addressService.findAreaByParentId(pId);
            return new ResultModel<>().success(areaList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }

    }

    /**
     *   添加收货地址
     * @param addressVo  传递参数
     * @param token  获取token
     * @param pro   省id
     * @param city 市 id
     * @param area 区 id
     * @return
     */
    @RequestMapping("add")
    public ResultModel addAddress(AddressVo addressVo, String token, Integer pro, Integer city, Integer area){
        try {
            Address address = new Address();
            BeanUtils.copyProperties(addressVo, address);
            //修改回显时用
            address.setProId(pro);
            address.setCityId(city);
            address.setAreaId(area);
           /* Integer [] arr = {pro,city,area};*/
            //传来的 省 市 县 对应 名称
            List<Area> list = addressService.findAreaNameById(pro, city, area);
            //声明空的字符串  组装数据
            String newAddress = "";
            for (Area i : list) {
                newAddress += i.getAreaName() +"-";
            }
            newAddress += address.getAddress();
            address.setAddress(newAddress);
            //id 不为空 修改   为空则是新增
            if(addressVo.getId() != null){
                addressService.update(address);
                return new ResultModel<>().success("success");
            }
            //保存新增
            addressService.add(address, token);

            return new ResultModel<>().success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultModel<>().error("异常" + e.getMessage());
        }
    }






}
