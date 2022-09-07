package com.a243.magicbazaar.dao.mapper.impl;

import com.a243.magicbazaar.dao.mapper.UserStarsMapper2;
import com.a243.magicbazaar.dao.util.BaseDao2;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("userStarsDao2")
public class UserStarsDaoImpl implements UserStarsMapper2 {

    @Override
    public List<Map<String, Object>> selectAll(String nickname,String order_number) {
        String sql="select s.id,u.nickname,o.order_number,s.stars,s.create_time,s.update_time\n" +
                "from mb_user_stars  s \n" +
                "left join mb_order_info i on s.order_info_id=i.id\n" +
                "left join mb_order o on o.id=i.order_id left join mb_user u on u.id=o.user_id where 1=1";
        if (null != nickname) {
            sql+=" and u.nickname like '%" + nickname + "%'";
        }
        if (null != order_number) {
            sql+=" and o.order_number like '%" + order_number + "%'";
        }
        return BaseDao2.query(sql,nickname,order_number);
    }

    @Override
    public List<Map<String, Object>> selectPages(Integer page, Integer limit, String nickname, String order_number) {
        String sql="select s.id,u.nickname,o.order_number,s.stars,s.create_time,s.update_time\n" +
                "from mb_user_stars  s \n" +
                "left join mb_order_info i on s.order_info_id=i.id\n" +
                "left join mb_order o on o.id=i.order_id left join mb_user u on u.id=o.user_id where 1=1";
        if (null != nickname) {
            sql+=" and u.nickname like '%" + nickname + "%'";
        }
        if (null != order_number) {
            sql+=" and o.order_number like '%" + order_number + "%'";
        }
        sql+=" limit ?,?";
        return BaseDao2.query(sql,(page-1)*limit,limit,nickname,order_number);
    }

    @Override
    public int delete(Integer id) {
        String sql="delete from mb_user_stars where id=?";
        return BaseDao2.update(sql,id);
    }
}
