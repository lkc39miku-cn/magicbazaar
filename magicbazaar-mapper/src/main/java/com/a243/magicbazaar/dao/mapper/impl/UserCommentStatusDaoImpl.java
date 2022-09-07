package com.a243.magicbazaar.dao.mapper.impl;


import com.a243.magicbazaar.dao.mapper.IUserCommentStatusDao;
import com.a243.magicbazaar.dao.util.BaseDao2;
import com.a243.magicbazaar.view.param.UserCommentStatusParam;
import org.springframework.stereotype.Repository;

@Repository("userCommentStatusDao")
public class UserCommentStatusDaoImpl implements IUserCommentStatusDao {
    @Override
    public int delete(Integer id) {
        String sql="delete from mb_user_comment_status where id=?";
        return BaseDao2.update(sql,id);
    }

    @Override
    public int update(UserCommentStatusParam commentStatusParam) {
        String sql="update mb_user_comment_status set comment_info=?,comment_start_time=?,comment_end_time=? where id=?";
        return BaseDao2.update(sql,commentStatusParam.getComment_info(),commentStatusParam.getComment_start_time(),commentStatusParam.getComment_end_time(),commentStatusParam.getId());
    }

    @Override
    public int updateon(UserCommentStatusParam commentStatusParam) {
        String sql="update mb_user_comment_status set comment_info=null,comment_start_time=null,comment_end_time=null where id=?";
        return BaseDao2.update(sql,commentStatusParam.getId());
    }
}
