package com.dj.mall.platform.web.mall.page;

import com.dj.mall.api.order.mall.AddressService;
import com.dj.mall.domain.mall.entiy.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/address/")
public class AddressPageController {

    @Autowired
    private AddressService addressService;

    /**
     * 去展示页面
     * @param token  带token
     * @return
     */
    @RequestMapping("toList")
    public String toList(String token){
        return "mall/address_list";
    }
    /**
     *  去添加收货地址
     * @param token  带token
     * @return
     */
    @RequestMapping("addAddress")
    public String addAddress(String token){
        return "mall/address_add";
    }
    /**
     * 去展示页面
     * @param token  带token
     * @return
     */
    @RequestMapping("toUpdate")
    public String toUpdate(String token, Integer id, ModelMap map){
        try {
            Address address = addressService.findAddressToUpdateById(id);
            String oldAddress = address.getAddress();
            String[] split = oldAddress.split("-");
            //江苏省-南京市-玄武区-aaaaaa  取 aaaaaa  重新赋值
            address.setAddress(split[split.length - 1]);
            map.put("address", address);
            map.put("oldAddress", oldAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "mall/address_update";
    }
}
