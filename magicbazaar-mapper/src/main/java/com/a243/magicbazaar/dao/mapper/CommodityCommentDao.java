package com.a243.magicbazaar.dao.mapper;

import com.a243.magicbazaar.dao.entity.CommodityComment;

import java.util.List;
import java.util.Map;

public interface CommodityCommentDao {
    List<Map<String,Object>> selectAll(CommodityComment comment);
    List<Map<String,Object>> selectPage(Integer page, Integer limit,CommodityComment comment);
    int update(Integer pid,Long id);
    int delete(Integer did,Long id);

}
