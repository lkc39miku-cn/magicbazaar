package com.a243.magicbazaar.dao.mapper;

import com.a243.magicbazaar.dao.entity.Address;

import java.util.List;
import java.util.Map;

public interface IAddressDao {
   List<Map<String,Object>> parent();
   List<Map<String,Object>> order(Integer pid);
   List<Map<String,Object>> findtwo(Integer id);

    int add(Integer parentMenu,Integer orderMenu,String name);
    int update(Integer id,Integer parentMenu,Integer orderMenu,String name);
    int delete(Integer id);

    Integer userAddressAdd(String addressinfo, Integer childMenu, String phone,Long id);
    List<Map<String,Object>> userAddressFindOne(Long id);
    Integer userAddressUpdate(String addressinfo, Integer childMenu, String phone,Integer id );
}
