package com.a243.magicbazaar.dao.mapper;

import com.a243.magicbazaar.view.param.UserCommentStatusParam;

public interface IUserCommentStatusDao {
    int delete(Integer id);
    int update(UserCommentStatusParam commentStatusParam);
    int updateon(UserCommentStatusParam commentStatusParam);
}
