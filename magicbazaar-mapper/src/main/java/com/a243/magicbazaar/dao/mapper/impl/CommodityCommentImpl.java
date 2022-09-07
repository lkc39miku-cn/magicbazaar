package com.a243.magicbazaar.dao.mapper.impl;

import com.a243.magicbazaar.dao.entity.CommodityComment;
import com.a243.magicbazaar.dao.mapper.CommodityCommentDao;
import com.a243.magicbazaar.dao.util.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository("commodityCommentImpl")
public class CommodityCommentImpl implements CommodityCommentDao {
    /**
     * 查询所有评论数据
     * @return
     */
    @Override
    public List<Map<String, Object>> selectAll(CommodityComment comment) {
        StringBuilder sql=new StringBuilder("select cc.*,c.title,u.nickname from mb_commodity_comment cc ,mb_commodity c," +
                "mb_user u where cc.commodity_id=c.id and cc.user_id=u.id");
        if(comment.getCommodityId()!=null){
            sql.append(" and commodity_id=").append(comment.getCommodityId());
        }
        if (comment.getUserId()!=null){
            sql.append(" and user_id=").append(comment.getUserId());
        }
        return BaseDao.find(sql.toString());
    }

    @Override
    public List<Map<String, Object>> selectPage(Integer page, Integer limit,CommodityComment comment) {
        StringBuffer sql=new StringBuffer("select cc.*,c.title,u.nickname from mb_commodity_comment cc ,mb_commodity c," +
                "mb_user u where cc.commodity_id=c.id and cc.user_id=u.id");
        if(comment.getCommodityId()!=null){
            sql.append(" and commodity_id="+comment.getCommodityId());
        }
        if (comment.getUserId()!=null){
            sql.append(" and user_id="+comment.getUserId());
        }
        sql.append(" limit ?,?");
        return BaseDao.find(sql.toString(),(page-1)*limit,limit);
    }

    @Override
    public int update(Integer pid,Long id) {
        String sql="update mb_commodity_comment set publish_status=? where id=? ";
        return BaseDao.update(sql,pid,id);
    }

    @Override
    public int delete(Integer did,Long id) {
        String sql="update mb_commodity_comment set delete_status=? where id=?";
        return BaseDao.update(sql,did,id);
    }
}
