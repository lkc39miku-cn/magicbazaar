package com.a243.magicbazaar.service.impl;

import com.a243.magicbazaar.dao.mapper.IAddressDao;
import com.a243.magicbazaar.service.AddressServices;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("addressServicesImpl")
public class AddressServicesImpl implements AddressServices {
    @Resource
     IAddressDao addressImpl;

    @Override
    public List<Map<String, Object>> parent() {
        return addressImpl.parent();
    }

    @Override
    public List<Map<String, Object>> order(Integer pid) {
        return addressImpl.order(pid);
    }

    @Override
    public List<Map<String, Object>> findtwo(Integer id) {
        return addressImpl.findtwo(id);
    }

    @Override
    public int add( Integer parentMenu,Integer orderMenu,String name) {
        return addressImpl.add(parentMenu,orderMenu,name);
    }

    @Override
    public int update(Integer id,Integer parentMenu,Integer orderMenu,String name) {
        return addressImpl.update(id,parentMenu,orderMenu,name);
    }

    @Override
    public int delete(Integer id) {
        return addressImpl.delete(id);
    }
}
