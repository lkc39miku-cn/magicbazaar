package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.Address;
import com.a243.magicbazaar.service.AddressService;
import com.a243.magicbazaar.service.AddressServices;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.AddressParam;
import com.a243.magicbazaar.view.vo.AddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("addressComment")
public class AddressController {
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Resource
    private AddressServices addressServicesImpl;

    @ResponseBody
    @RequestMapping(value = "list")
    public Result<List<AddressVo>> list(AddressParam addressParam) {
        return addressService.list(addressParam);
    }

    @RequestMapping("find")
    @ResponseBody
    public List<Map<String,Object>> find(){
        return addressServicesImpl.parent();
    }
    @RequestMapping("finds")
    @ResponseBody
    public List<Map<String,Object>> finds(Integer pid){
        return addressServicesImpl.order(pid);
    }
    @RequestMapping("findtwo")
    @ResponseBody
    public List<Map<String,Object>> findtwo(Integer id){
        return addressServicesImpl.findtwo(id);
    }
    @RequestMapping("add")
    @ResponseBody
    public Integer add(Integer parentMenu,Integer orderMenu,String name){
        return addressServicesImpl.add(parentMenu, orderMenu, name);
    }
    @RequestMapping("update")
    @ResponseBody
    public Integer update(Integer id,Integer parentMenu,Integer orderMenu,String name){
        return addressServicesImpl.update(id, parentMenu,orderMenu,name);
    }
    @RequestMapping("del")
    @ResponseBody
    public Integer del(Integer id){
        return addressServicesImpl.delete(id);
    }
}
