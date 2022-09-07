package com.a243.magicbazaar.dao.mapper.impl;

import com.a243.magicbazaar.dao.entity.Address;
import com.a243.magicbazaar.dao.mapper.AddressMapper;
import com.a243.magicbazaar.dao.mapper.IAddressDao;
import com.a243.magicbazaar.dao.util.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("addressImpl")
public class AddressImplDao implements IAddressDao {
    @Override
    public List<Map<String, Object>> parent() {
        String sql="select * from mb_address where parent_id=0";
        return BaseDao.find(sql);
    }

    @Override
    public List<Map<String, Object>> order(Integer pid) {
        String sql="select * from mb_address where parent_id=?";
        return BaseDao.find(sql,pid);
    }



    @Override
    public List<Map<String, Object>> findtwo(Integer id) {
        String sql="select * from mb_address where id=?";
//        List<Map<String,Object>> list=BaseDao.find(sql,id);
//        Integer ids=0;
//        if (list!=null&&list.size()>0){
//            for (Map a: list) {
//                ids= (Integer) a.get("parent_id");
//            }
//        }
//        String sql2="select * from mb_address where id="+ids;
        return BaseDao.find(sql,id);
    }


    @Override
    public int add(Integer parentMenu,Integer orderMenu,String name) {
        if(parentMenu==null){
            String sqls="select * from mb_address where name=?";
            List<Map<String,Object>> list=BaseDao.find(sqls,name);
            if(list!=null&&list.size()>0){
                return 3;
            }else {
                String sql="insert into mb_address(name,parent_id,level,create_time,update_time) values(?,0,0,now(),now())";
                return BaseDao.update(sql,name);
            }
        }
        if(parentMenu!=null&&orderMenu==null){
            String sqls="select * from  mb_address where name=? and parent_id=?";
            List<Map<String,Object>> list=BaseDao.find(sqls,name,parentMenu);
            if(list!=null&&list.size()>0){
                return 3;
            }else {
                String sql = "insert into mb_address(name,parent_id,level,create_time,update_time) values(?,?,1,now(),now())";
                return BaseDao.update(sql, name, parentMenu);
            }
        }
        if(orderMenu!=null){
            String sqls="select * from  mb_address where name=? and parent_id=?";
            List<Map<String,Object>> list=BaseDao.find(sqls,name,orderMenu);
            if(list!=null&&list.size()>0){
                return 3;
            }else {
                String sql = "insert into mb_address(name,parent_id,level,create_time,update_time) values(?,?,1,now(),now())";
                return BaseDao.update(sql, name, orderMenu);
            }
        }
        return 0;
    }

    @Override
    public int update(Integer id,Integer parentMenu,Integer orderMenu,String name) {
        if(parentMenu==null){
            String sql="update mb_address set name=?,parent_id=0,create_time=now(),update_time=now() where id=?";
            return BaseDao.update(sql,name,id);
        }
        if(parentMenu.toString().length()>0&&orderMenu==null){
            String sql="update mb_address set name=?,parent_id=?,create_time=now(),update_time=now() where id=?";
            return BaseDao.update(sql,name,parentMenu,id);
        }
        if(orderMenu.toString().length()>0){
            String sql="update mb_address set name=?,parent_id=?,create_time=now(),update_time=now() where id=?";
            return BaseDao.update(sql,name,orderMenu,id);
        }
        return 0;
    }

    @Override
    public int delete(Integer id) {
        String sql="delete from mb_address where id=?";
        return BaseDao.update(sql,id);
    }

    @Override
    public Integer userAddressAdd(String addressinfo, Integer childMenu, String phone,Long id) {
        StringBuffer sql=new StringBuffer("insert into mb_user_address(user_id,phone,address_id," +
                "address_info,address_status,create_time,update_time) values(?,?,?,?,0,now(),now())");

        return BaseDao.update(sql.toString(),id,phone,childMenu,addressinfo);
    }

    @Override
    public List<Map<String,Object>> userAddressFindOne(Long id) {
        StringBuffer sql=new StringBuffer("select * from mb_user_address where id=?");
        return BaseDao.find(sql.toString(),id);
    }

    @Override
    public Integer userAddressUpdate(String addressinfo, Integer childMenu, String phone, Integer id) {
        StringBuffer sql=new StringBuffer("update mb_user_address set phone=?,address_id=?,address_info=?," +
                "create_time=now(),update_time=now() where id=?");
        return BaseDao.update(sql.toString(),phone,childMenu,addressinfo,id);
    }
}
